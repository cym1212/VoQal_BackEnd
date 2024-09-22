package Capstone.VoQal.domain.member.service;

import Capstone.VoQal.domain.chatting.dto.ChatRoom;
import Capstone.VoQal.domain.member.domain.QCoachAndStudent;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ExecutionException;


@Service
@RequiredArgsConstructor
public class CoachAndStudentService {

    private final JPAQueryFactory queryFactory;

    private static final String CHAT_COLLECTION = "chats";

    @Transactional
    public void deleteByCoachIdAndStudentId(Long coachId, Long studentId) throws ExecutionException, InterruptedException {
        // 1. Firestore에서 학생이 포함된 채팅방 삭제
        deleteChatRoomsForStudent(coachId, studentId);

        // 2. PostgreSQL에서 학생과 코치 간의 관계 삭제
        QCoachAndStudent coachAndStudent = QCoachAndStudent.coachAndStudent;
        long deletedCount = queryFactory.delete(coachAndStudent)
                .where(coachAndStudent.coach.member.id.eq(coachId)
                        .and(coachAndStudent.student.member.id.eq(studentId)))
                .execute();
    }

    private void deleteChatRoomsForStudent(Long coachId, Long studentId) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference chatRooms = dbFirestore.collection(CHAT_COLLECTION);

        // 학생 ID로 채팅방을 조회
        ApiFuture<QuerySnapshot> future = chatRooms
                .whereArrayContains("participants", studentId.toString())
                .get();

        List<ChatRoom> rooms = future.get().toObjects(ChatRoom.class);

        // 조회된 채팅방 중 코치와 학생이 모두 포함된 채팅방 삭제
        for (ChatRoom room : rooms) {
            if (room.getParticipants().contains(coachId.toString()) && room.getParticipants().contains(studentId.toString())) {
                // 1. 하위 컬렉션인 "messages" 삭제
                deleteMessagesInChatRoom(room.getId());

                // 2. 상위 채팅방 문서 삭제
                chatRooms.document(room.getId()).delete().get();  // 채팅방 삭제
                System.out.println("채팅방 삭제 성공: " + room.getId());
            }
        }
    }

    // 특정 채팅방 내의 하위 컬렉션 "messages" 삭제
    private void deleteMessagesInChatRoom(String chatRoomId) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference messages = dbFirestore.collection(CHAT_COLLECTION).document(chatRoomId).collection("messages");

        // "messages" 하위 컬렉션의 모든 문서 조회
        ApiFuture<QuerySnapshot> future = messages.get();
        List<QueryDocumentSnapshot> messageDocs = future.get().getDocuments();

        // 하위 문서 모두 삭제
        for (QueryDocumentSnapshot doc : messageDocs) {
            messages.document(doc.getId()).delete().get();  // 메시지 삭제
            System.out.println("메시지 삭제 성공: " + doc.getId());
        }
    }
}