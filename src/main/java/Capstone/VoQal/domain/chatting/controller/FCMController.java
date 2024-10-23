package Capstone.VoQal.domain.chatting.controller;


import Capstone.VoQal.domain.chatting.dto.FCMTokenSaveRequestDTO;
import Capstone.VoQal.domain.chatting.dto.NotificationRequestDTO;
import Capstone.VoQal.domain.chatting.service.FCMService;
import Capstone.VoQal.global.dto.MessageDTO;
import Capstone.VoQal.global.error.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fcm")
@RequiredArgsConstructor
public class FCMController {

    private final FCMService fcmService;

    @PostMapping("/token")
    @Operation(summary = "fcm 토큰 저장", description = "프론트에서 보낸 fcm토큰을 저장합니다.")
    public MessageDTO saveFcmToken(@RequestBody FCMTokenSaveRequestDTO fcmTokenSaveRequestDTO) {
        fcmService.saveFcmToken(fcmTokenSaveRequestDTO);
        return MessageDTO.builder()
                .message("FCM 토큰이 성공적으로 저장되었습니다")
                .status(200)
                .build();
    }

    @PostMapping("/send")
    @Operation(summary = "학생입장 - 채팅방 id 조회 (없으면 생성)", description = "학생입장 - 코치와의 채팅 방이 존재하는지 확인한 후 있으면 채팅방 id 리턴 없으면 생성후 리턴합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 멤버 ID 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "400", description = "푸시알림 전송을 실패했습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
    public MessageDTO sendNotification(@RequestBody NotificationRequestDTO notificationRequestDTO) {
        fcmService.sendNotification(notificationRequestDTO);
        return MessageDTO.builder()
                .message("알림을 성공적으로 요청했습니다")
                .status(200)
                .build();
    }
}
