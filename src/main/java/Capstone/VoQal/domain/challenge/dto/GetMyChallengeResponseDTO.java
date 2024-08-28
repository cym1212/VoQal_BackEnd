package Capstone.VoQal.domain.challenge.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetMyChallengeResponseDTO {
    private String thumbnailUrl;
    private String recordUrl;
    private Long challengePostId;
    private String songTitle;
    private String singer;
    private String nickName;
    private LocalDateTime createdAt;
    private Long likeCount;

}
