package Capstone.VoQal.global.auth.dto;


import Capstone.VoQal.global.enums.Role;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberInfromationDTO {

    private Long memberId;
    private String nickName;
    private String email;
    private String name;
    private String phoneNum;
    private Role role;
    private String assignedCoach;
    private String lessonSongUrl;

    // 필요 시 별도의 메서드를 추가하여 새로운 인스턴스를 반환하는 방식 사용
    public MemberInfromationDTO withAssignedCoach(String assignedCoach) {
        return MemberInfromationDTO.builder()
                .memberId(this.memberId)
                .nickName(this.nickName)
                .email(this.email)
                .name(this.name)
                .phoneNum(this.phoneNum)
                .role(this.role)
                .assignedCoach(assignedCoach)
                .lessonSongUrl(this.lessonSongUrl)
                .build();
    }

    public MemberInfromationDTO withLessonSongUrl(String lessonSongUrl) {
        return MemberInfromationDTO.builder()
                .memberId(this.memberId)
                .nickName(this.nickName)
                .email(this.email)
                .name(this.name)
                .phoneNum(this.phoneNum)
                .role(this.role)
                .assignedCoach(this.assignedCoach)
                .lessonSongUrl(lessonSongUrl)
                .build();
    }
}
