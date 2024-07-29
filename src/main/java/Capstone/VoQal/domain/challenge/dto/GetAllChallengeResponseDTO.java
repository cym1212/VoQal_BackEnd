package Capstone.VoQal.domain.challenge.dto;


import Capstone.VoQal.domain.challenge.domain.ChallengePost;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAllChallengeResponseDTO {

    private String todayKeyword;
    private String thumbnailUrl;
    private String recordUrl;
    private String songTitle;
    private String singer;
    private String nickName;

}
