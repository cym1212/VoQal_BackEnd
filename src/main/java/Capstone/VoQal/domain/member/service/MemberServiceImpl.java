package Capstone.VoQal.domain.member.service;

import Capstone.VoQal.domain.member.domain.CoachAndStudent;
import Capstone.VoQal.domain.member.domain.Member;
import Capstone.VoQal.domain.member.domain.Student;
import Capstone.VoQal.domain.member.repository.CoachAndStudent.CoachAndStudentRepository;
import Capstone.VoQal.domain.member.repository.Member.MemberRepository;
import Capstone.VoQal.global.enums.Role;
import Capstone.VoQal.global.jwt.service.JwtTokenIdDecoder;
import Capstone.VoQal.global.enums.ErrorCode;
import Capstone.VoQal.global.error.exception.BusinessException;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;


@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final JwtTokenIdDecoder jwtTokenIdDecoder;
    private final MemberRepository memberRepository;
    private final CoachAndStudentRepository coachAndStudentRepository;
    private static final String CHAT_COLLECTION = "chats";

    @Override
    public Member getCurrentCoach() {
        Member coachMember = getCurrentMember();

        if (coachMember.getRole() != Role.COACH) {
            throw new BusinessException(ErrorCode.NOT_COACH);
        }
        return coachMember;
    }

    @Override
    public Member getStudent(Long studentrId) {
        Member studentMember = getMemberById(studentrId);

        if (studentMember.getRole() != Role.STUDENT) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
        }
        return studentMember;
    }


    @Override
    public Member getCurrentMember() {
        long memberId = jwtTokenIdDecoder.getCurrentUserId();
        return getMemberById(memberId);
    }

    @Override
    public Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_MEMBER_ID));
    }


    @Override
    public CoachAndStudent getCoachAndStudent(Long coachId, Long studentId) {
        return coachAndStudentRepository.findByCoachIdAndStudentId(coachId, studentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REQUEST));
    }
    @Override
    public CoachAndStudent getCoachAndStudentWithSignUp(Long coachId, Long studentId) {
        return coachAndStudentRepository.findByCoachIdAndStudentIdWithPendingStatus(coachId, studentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REQUEST));
    }

    @Override
    public void validateStudentEntity(Student student) {
        if (student == null) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
        }
    }

    @Override
    public Long getCoachIdByStudentId(Long studentId) {
        Long coachIdByStudentId = coachAndStudentRepository.findCoachIdByStudentId(studentId);
        if (coachIdByStudentId == null) {
            throw new BusinessException(ErrorCode.COACH_NOT_FOUND);
        }
        return coachIdByStudentId;
    }

    @Transactional
    @Override
    public void deleteMember() throws ExecutionException, InterruptedException {
        Long memberId = getCurrentMember().getId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_MEMBER_ID));

        // 1. Firestore에서 해당 사용자가 참가자로 포함된 모든 채팅방과 그 내역을 삭제
        deleteAllChatRoomsForMember(memberId);

        // 2. PostgreSQL에서 회원 정보 삭제
        memberRepository.delete(member);
        System.out.println("회원 삭제 성공: " + memberId);
    }

    /**
     * Firestore에서 회원이 포함된 모든 채팅방을 삭제하는 메서드
     * @param memberId 탈퇴하는 회원의 ID
     */
    private void deleteAllChatRoomsForMember(Long memberId) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference chatRooms = dbFirestore.collection(CHAT_COLLECTION);

        // 해당 회원이 참가자로 있는 모든 채팅방을 조회
        ApiFuture<QuerySnapshot> future = chatRooms
                .whereArrayContains("participants", memberId.toString())
                .get();

        List<QueryDocumentSnapshot> chatRoomDocs = future.get().getDocuments();
        System.out.println("조회된 채팅방 개수: " + chatRoomDocs.size());

        // 조회된 각 채팅방을 삭제
        for (QueryDocumentSnapshot chatRoomDoc : chatRoomDocs) {
            String chatRoomId = chatRoomDoc.getId();

            // 1. 채팅방 내 messages 하위 컬렉션 삭제
            deleteMessagesInChatRoom(chatRoomId);

            // 2. 채팅방 문서 삭제
            chatRooms.document(chatRoomId).delete().get();
            System.out.println("채팅방 삭제 성공: " + chatRoomId);
        }
    }

    /**
     * 특정 채팅방 내의 messages 하위 컬렉션 삭제
     * @param chatRoomId 삭제할 채팅방의 ID
     */
    private void deleteMessagesInChatRoom(String chatRoomId) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference messages = dbFirestore.collection(CHAT_COLLECTION).document(chatRoomId).collection("messages");

        // 채팅방 내의 messages 하위 컬렉션 문서들 조회
        ApiFuture<QuerySnapshot> future = messages.get();
        List<QueryDocumentSnapshot> messageDocs = future.get().getDocuments();

        // 하위 문서 모두 삭제
        for (QueryDocumentSnapshot messageDoc : messageDocs) {
            messages.document(messageDoc.getId()).delete().get();  // 메시지 삭제
            System.out.println("메시지 삭제 성공: " + messageDoc.getId());
        }
    }
}
