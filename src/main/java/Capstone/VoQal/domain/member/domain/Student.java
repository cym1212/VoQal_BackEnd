package Capstone.VoQal.domain.member.domain;


import Capstone.VoQal.domain.lesson.note.domain.LessonNote;
import Capstone.VoQal.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "student")
public class Student extends BaseEntity {

    @Column
    private String lessonSong;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    @OneToMany( fetch = FetchType.LAZY)
    @JoinColumn(name = "note_id")
    private List<LessonNote> lessonNote;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<CoachAndStudent> coachAndStudents;


}
