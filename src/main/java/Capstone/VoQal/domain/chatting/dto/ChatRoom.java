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
public class ChatRoom {
    private String id;
    private List<String> participants; // A와 B의 사용자 ID 리스트
    private long lastMessageTimestamp;
}
