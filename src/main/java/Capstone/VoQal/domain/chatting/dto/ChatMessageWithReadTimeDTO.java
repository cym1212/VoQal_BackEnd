package Capstone.VoQal.domain.chatting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageWithReadTimeDTO {
    private Long coachLastReadTime;
    private Long studentLastReadTime;
    private List<ChatMessageResponse> messages;
}
