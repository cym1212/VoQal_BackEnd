package Capstone.VoQal.domain.chatting.controller;


import Capstone.VoQal.domain.chatting.dto.ChatMessageRequest;
import Capstone.VoQal.domain.chatting.dto.ChatMessageResponse;
import Capstone.VoQal.domain.chatting.dto.ChatMessageWithReadTimeDTO;
import Capstone.VoQal.domain.chatting.dto.ChatRoomInfo;
import Capstone.VoQal.domain.chatting.service.ChatService;
import Capstone.VoQal.global.dto.MessageDTO;
import Capstone.VoQal.global.dto.ResponseWrapper;
import Capstone.VoQal.global.error.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    // 채팅방 생성 또는 가져오기
    @PostMapping("/room/{studentId}")
    @Operation(summary = "채팅방 id 조회(없으면 생성) - 코치 입장", description = "코치 입장 - 담당 학생의 id를 입력하면 그 학생과의 채팅방이 존재하는지 확인한 후 있으면 채팅방 id를 리턴 없으면 생성후 리턴합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "400", description = "코치가 아닙니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
    public ResponseEntity<ResponseWrapper<String>> getOrCreateChatRoom(@PathVariable(value = "studentId")Long studentId) throws ExecutionException, InterruptedException {

        String id = chatService.getOrCreateChatRoom(studentId).getId();
        ResponseWrapper<String> result = ResponseWrapper.<String>builder()
                .status(HttpStatus.OK.value())
                .data(id)
                .build();
        return ResponseEntity.ok(result);
    }

    // 메시지 전송
    @PostMapping("/{chatId}/message")
    @Operation(summary = "메시지 전송 로직", description = "메시지를 전송합니다.")
    public MessageDTO sendMessage(@PathVariable(value = "chatId") String chatId, @RequestBody ChatMessageRequest message) {
        chatService.sendMessage(chatId, message);
        return MessageDTO.builder()
                .status(HttpStatus.OK.value())
                .message("성공적으로 전송되었습니다.")
                .build();
    }

    //  메시지 가져오기
    @GetMapping("/{chatId}/messages")
    @Operation(summary = "메시지 내역 조회 로직", description = "기존의 메시지 전송 내역을 가져옵니다.")
    public ResponseEntity<ResponseWrapper<ChatMessageWithReadTimeDTO>> getMessages(@PathVariable(value = "chatId") String chatId) throws ExecutionException, InterruptedException {
        ChatMessageWithReadTimeDTO messages = chatService.getMessagesWithReadTimes(chatId);
        ResponseWrapper<ChatMessageWithReadTimeDTO> result = ResponseWrapper.<ChatMessageWithReadTimeDTO>builder()
                .status(HttpStatus.OK.value())
                .data(messages)
                .build();
        return ResponseEntity.ok(result);
    }

    //학생입장
    @PostMapping("/room")
    @Operation(summary = "학생입장 - 채팅방 id 조회 (없으면 생성)", description = "학생입장 - 코치와의 채팅 방이 존재하는지 확인한 후 있으면 채팅방 id 리턴 없으면 생성후 리턴합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 멤버 ID 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
    public ResponseEntity<ResponseWrapper<Map<String, Object>>> getOrCreateChatRoomForStudent() throws ExecutionException, InterruptedException {
        ChatRoomInfo chatRoomInfo = chatService.getOrCreateChatRoomForStudent();

        Map<String, Object> response = new HashMap<>();
        response.put("chatRoomId", chatRoomInfo.getChatRoomRef().getId());
        response.put("coachId", chatRoomInfo.getCoachId());

        ResponseWrapper<Map<String, Object>> result = ResponseWrapper.<Map<String, Object>>builder()
                .status(HttpStatus.OK.value())
                .data(response)
                .build();

        return ResponseEntity.ok(result);
    }
}
