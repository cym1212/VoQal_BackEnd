package Capstone.VoQal.domain.member.domain;

import Capstone.VoQal.domain.lesson.note.domain.LessonNote;
import Capstone.VoQal.global.domain.BaseEntity;
import Capstone.VoQal.global.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "coach_and_student")
public class CoachAndStudent extends BaseEntity {



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coach_id", referencedColumnName = "member_id")
    private Coach coach;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", referencedColumnName = "member_id")
    private Student student;


    @Enumerated(EnumType.STRING)
    @Setter
    private RequestStatus status;

    @OneToMany(mappedBy = "coachAndStudent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<LessonNote> lessonNotes;

    private String lessonSongUrl;

    public void updateLessonSongUrl(String updateLessonSongUrl) {
        this.lessonSongUrl = updateLessonSongUrl;
    }

}
