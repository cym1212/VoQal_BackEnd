package Capstone.VoQal.domain.chatting.service;

import Capstone.VoQal.domain.chatting.dto.*;
import Capstone.VoQal.domain.member.domain.Member;
import Capstone.VoQal.domain.member.repository.CoachAndStudent.CoachAndStudentRepository;
import Capstone.VoQal.domain.member.service.MemberService;
import Capstone.VoQal.global.enums.ErrorCode;
import Capstone.VoQal.global.error.exception.BusinessException;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
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

        // 기존 채팅방 조회 (학생 ID가 포함된 방)
        ApiFuture<QuerySnapshot> future = chatRooms
                .whereArrayContains("participants", studentIdString)
                .get();

        List<ChatRoom> rooms = future.get().toObjects(ChatRoom.class);

        // 현재 코치와 학생이 포함된 채팅방이 있는지 확인
        for (ChatRoom room : rooms) {
            if (room.getParticipants().contains(currentMemberId) && room.getParticipants().contains(studentIdString)) {
                return chatRooms.document(room.getId());  // 기존 채팅방 반환
            }
        }

        // 새로운 채팅방 ID를 코치ID_학생ID 형식으로 생성
        String chatRoomId = currentMemberId + "_" + studentIdString;
        DocumentReference newRoom = chatRooms.document(chatRoomId);

        // 새로운 채팅방 생성
        ChatRoom chatRoom = new ChatRoom(chatRoomId, List.of(studentIdString, currentMemberId), System.currentTimeMillis(), null, null);
        newRoom.set(chatRoom).get();  // Firestore에 저장 완료 후 반환

        return newRoom;
    }


    // 채팅방 찾기 (학생입장)
    public ChatRoomInfo getOrCreateChatRoomForStudent() throws ExecutionException, InterruptedException {
        Long currentMemberId = memberService.getCurrentMember().getId();
        Long coachId = coachAndStudentRepository.findCoachIdByStudentId(currentMemberId);

        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference chatRooms = dbFirestore.collection(CHAT_COLLECTION);

        // 기존 채팅방 조회 (현재 학생이 포함된 방)
        ApiFuture<QuerySnapshot> future = chatRooms
                .whereArrayContains("participants", currentMemberId.toString())
                .get();

        List<ChatRoom> rooms = future.get().toObjects(ChatRoom.class);

        // 현재 학생과 코치가 포함된 채팅방이 있는지 확인
        for (ChatRoom room : rooms) {
            if (room.getParticipants().contains(coachId.toString()) && room.getParticipants().contains(currentMemberId.toString())) {
                return new ChatRoomInfo(chatRooms.document(room.getId()), coachId);  // 기존 채팅방이 있을 경우 반환
            }
        }

        // 새로운 채팅방 ID를 "coachId_studentId" 형식으로 생성
        String chatRoomId = coachId.toString() + "_" + currentMemberId.toString();
        DocumentReference newRoom = chatRooms.document(chatRoomId);


        // 새로운 채팅방 생성
        ChatRoom chatRoom = new ChatRoom(chatRoomId, List.of(currentMemberId.toString(), coachId.toString()), System.currentTimeMillis(), null, null);

        // Firestore에 채팅방 저장 완료 대기
        newRoom.set(chatRoom).get();

        // 새로 생성된 채팅방 정보 반환
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
        Member currentMember = memberService.getCurrentMember();
        Member receiver = memberService.getMemberById(Long.valueOf(message.getReceiverId()));

        if (receiver != null) {
            String fcmToken = receiver.getFcmToken();

            if (fcmToken != null) {
                sendPushNotification(fcmToken,currentMember.getId().toString(), currentMember.getName(), message.getMessage());
            }
        }
    }

    public void sendPushNotification(String fcmToken,String senderId, String title, String message) {
        Message fcmMessage = Message.builder()
                .setToken(fcmToken)
                .putData("sender", senderId)
                .putData("title", title)
                .putData("message", message)
                .setApnsConfig(ApnsConfig.builder()
                        .putHeader("apns-priority", "10")
                        .setAps(Aps.builder()
                                .setAlert(ApsAlert.builder()
                                        .setTitle(title)
                                        .setBody(message)
                                        .build())
                                .setSound("default")
                                .build())
                        .build())
                .build();

        try {
            FirebaseMessaging.getInstance().send(fcmMessage);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.FAIL_TO_SEND_PUSH_NOTIFICATION);
        }
    }


    public ChatMessageWithReadTimeDTO getMessagesWithReadTimes(String chatId) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();

        // Firestore 병렬 호출을 위한 CompletableFuture
        CompletableFuture<DocumentSnapshot> chatDocumentFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return dbFirestore.collection(CHAT_COLLECTION).document(chatId).get().get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        CompletableFuture<List<QueryDocumentSnapshot>> messagesFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return dbFirestore.collection(CHAT_COLLECTION).document(chatId).collection("messages")
                        .orderBy("timestamp", Query.Direction.ASCENDING)
                        .get().get().getDocuments();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // 두 Future가 완료될 때까지 기다림
        DocumentSnapshot chatDocument = chatDocumentFuture.get();
        List<QueryDocumentSnapshot> messagesDocs = messagesFuture.get();

        // coachLastReadTime 및 studentLastReadTime 가져오기
        Long coachLastReadTime = chatDocument.getLong("coachLastReadTime");
        Long studentLastReadTime = chatDocument.getLong("studentLastReadTime");

        // 메시지 리스트 생성
        List<ChatMessageResponse> messages = new ArrayList<>();
        for (QueryDocumentSnapshot messageDoc : messagesDocs) {
            ChatMessageResponse chatMessageResponse = ChatMessageResponse.builder()
                    .receiverId(messageDoc.getString("receiverId"))
                    .message(messageDoc.getString("message"))
                    .timestamp(messageDoc.getLong("timestamp"))
                    .build();
            messages.add(chatMessageResponse);
        }

        // 결과 반환
        return ChatMessageWithReadTimeDTO.builder()
                .coachLastReadTime(coachLastReadTime)
                .studentLastReadTime(studentLastReadTime)
                .messages(messages)
                .build();
    }

}
