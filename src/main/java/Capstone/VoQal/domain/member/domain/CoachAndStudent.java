package Capstone.VoQal.domain.member.domain;

import Capstone.VoQal.global.domain.BaseEntity;
import Capstone.VoQal.global.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "coach_and_student")
public class CoachAndStudent extends BaseEntity {



//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "coach_id", referencedColumnName = "id")
//    private Coach coach;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "student_id", referencedColumnName = "id")
//    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coach_member_id", referencedColumnName = "id")
    private Member coachMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_member_id", referencedColumnName = "id")
    private Member studentMember;

    @Enumerated(EnumType.STRING)
    @Setter
    private RequestStatus status;

    private String lessonSongUrl;



}
