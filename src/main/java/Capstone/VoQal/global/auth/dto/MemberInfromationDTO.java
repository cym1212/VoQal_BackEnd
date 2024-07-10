package Capstone.VoQal.global.auth.dto;


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
    private String role;
    private int status;
}
