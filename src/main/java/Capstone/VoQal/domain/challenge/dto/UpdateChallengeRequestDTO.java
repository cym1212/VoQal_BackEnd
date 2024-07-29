package Capstone.VoQal.domain.challenge.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateChallengeRequestDTO {

    private String updateSongTitle;
    private String updateSinger;
}
