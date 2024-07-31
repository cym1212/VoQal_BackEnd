package Capstone.VoQal.domain.challenge.dto;


import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAllChallengeResponseDTO {

    private Long challengeId;
    @Setter
    private String todayKeyword;
    private String thumbnailUrl;
    private String recordUrl;
    private String songTitle;
    private String singer;
    private String nickName;
    private boolean liked;

    public GetAllChallengeResponseDTO(Long challengeId, String thumbnailUrl, String recordUrl, String songTitle, String singer, String nickName, boolean liked) {
        this.challengeId = challengeId;
        this.thumbnailUrl = thumbnailUrl;
        this.recordUrl = recordUrl;
        this.songTitle = songTitle;
        this.singer = singer;
        this.nickName = nickName;
        this.liked = liked;
    }

}
