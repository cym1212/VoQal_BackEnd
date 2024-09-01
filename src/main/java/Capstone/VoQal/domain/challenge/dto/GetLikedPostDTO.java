package Capstone.VoQal.domain.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetLikedPostDTO {
    private Long challengeId;

    private String nickName;

    private String challengeRecordUrl;

    private String thumbnailUrl;

    private String songTitle;

    private String singer;
}
