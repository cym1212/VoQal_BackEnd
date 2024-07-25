package Capstone.VoQal.global.auth.dto;


import Capstone.VoQal.global.enums.Role;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberInfromationDTO {

    private String nickName;
    private String email;
    private String name;
    private String phoneNum;
    private Role role;
    private String lessonSongUrl;
}
