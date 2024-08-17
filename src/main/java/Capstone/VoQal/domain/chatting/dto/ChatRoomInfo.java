package Capstone.VoQal.domain.chatting.dto;

import com.google.cloud.firestore.DocumentReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomInfo {
    private DocumentReference chatRoomRef;
    private Long coachId;
}
