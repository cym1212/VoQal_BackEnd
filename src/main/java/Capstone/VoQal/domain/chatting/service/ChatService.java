package Capstone.VoQal.domain.chatting.service;

import Capstone.VoQal.domain.chatting.dto.ChatMessageRequest;
import Capstone.VoQal.domain.chatting.dto.ChatMessageResponse;
import Capstone.VoQal.domain.chatting.dto.ChatRoom;
import Capstone.VoQal.domain.chatting.dto.ChatRoomInfo;
import Capstone.VoQal.domain.member.repository.CoachAndStudent.CoachAndStudentRepository;
import Capstone.VoQal.domain.member.service.MemberService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final MemberService memberService;
    private final CoachAndStudentRepository coachAndStudentRepository;
    private static final String CHAT_COLLECTION = "chats";

    // 1대1 채팅방 생성 또는 가져오기 (코치 입장)
    public DocumentReference getOrCreateChatRoom(Long studentId) throws ExecutionException, InterruptedException {
        String currentMemberId = memberService.getCurrentCoach().getId().toString();
        String studentIdString = studentId.toString();

        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference chatRooms = dbFirestore.collection(CHAT_COLLECTION);

        ApiFuture<QuerySnapshot> future = chatRooms
                .whereArrayContains("participants", studentIdString)
                .get();

        List<ChatRoom> rooms = future.get().toObjects(ChatRoom.class);

        for (ChatRoom room : rooms) {
            if (room.getParticipants().contains(currentMemberId) && room.getParticipants().contains(studentIdString)) {
                return chatRooms.document(room.getId());
            }
        }

        DocumentReference newRoom = chatRooms.document();
        ChatRoom chatRoom = new ChatRoom(newRoom.getId(), List.of(studentIdString, currentMemberId), System.currentTimeMillis());
        newRoom.set(chatRoom);
        return newRoom;
    }

    // 채팅방 찾기 (학생입장)
    public ChatRoomInfo getOrCreateChatRoomForStudent() throws ExecutionException, InterruptedException {
        Long currentMemberId = memberService.getCurrentMember().getId();
        Long coachId = coachAndStudentRepository.findCoachIdByStudentId(currentMemberId);

        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference chatRooms = dbFirestore.collection(CHAT_COLLECTION);

        ApiFuture<QuerySnapshot> future = chatRooms
                .whereArrayContains("participants", currentMemberId.toString())
                .get();

        List<ChatRoom> rooms = future.get().toObjects(ChatRoom.class);

        for (ChatRoom room : rooms) {
            if (room.getParticipants().contains(coachId.toString()) && room.getParticipants().contains(currentMemberId.toString())) {
                return new ChatRoomInfo(chatRooms.document(room.getId()), coachId);  // ChatRoomInfo 객체 반환
            }
        }

        DocumentReference newRoom = chatRooms.document();
        ChatRoom chatRoom = new ChatRoom(newRoom.getId(), List.of(currentMemberId.toString(), coachId.toString()), System.currentTimeMillis());
        newRoom.set(chatRoom);
        return new ChatRoomInfo(newRoom, coachId);
    }



    public void sendMessage(String chatId, ChatMessageRequest message) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference messages = dbFirestore.collection(CHAT_COLLECTION).document(chatId).collection("messages");

        long currentTimestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

        ChatMessageResponse chatMessageWithTimestamp = ChatMessageResponse.builder()
                .receiverId(message.getReceiverId())
                .message(message.getMessage())
                .timestamp(currentTimestamp)
                .build();

        messages.add(chatMessageWithTimestamp);

        dbFirestore.collection(CHAT_COLLECTION).document(chatId).update("lastMessageTimestamp", currentTimestamp);
    }


    public List<ChatMessageResponse> getMessages(String chatId) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = dbFirestore.collection(CHAT_COLLECTION).document(chatId).collection("messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)  // 오래된 순서대로 정렬
                .get();
        return future.get().toObjects(ChatMessageResponse.class);
    }

}
