package Capstone.VoQal.domain.chatting.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FCMTokenSaveRequestDTO {
    private String  userId;
    private String fcmToken;

}
