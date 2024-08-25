package Capstone.VoQal.domain.challenge.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetAllChallengeResponseDTO {

    private Long challengeId;
    private String thumbnailUrl;
    private String recordUrl;
    private String songTitle;
    private String singer;
    private String nickName;
    private LocalDateTime createdAt;
    private boolean liked;

//    public GetAllChallengeResponseDTO(Long challengeId, String thumbnailUrl, String recordUrl, String songTitle, String singer, String nickName, boolean liked) {
//        this.challengeId = challengeId;
//        this.thumbnailUrl = thumbnailUrl;
//        this.recordUrl = recordUrl;
//        this.songTitle = songTitle;
//        this.singer = singer;
//        this.nickName = nickName;
//        this.liked = liked;
//    }

}
