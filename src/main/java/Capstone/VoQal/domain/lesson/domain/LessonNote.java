package Capstone.VoQal.domain.lesson.domain;


import Capstone.VoQal.domain.member.domain.Student;
import Capstone.VoQal.domain.member.domain.Trainer;
import Capstone.VoQal.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "lesson_note")
public class LessonNote extends BaseEntity {

    @Column(nullable = false)
    private String songTitle;

    @Column(nullable = false)
    private String lessonNoteTitle;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private String recordUrl;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @OneToOne(mappedBy = "lessonNote", fetch = FetchType.LAZY)
    private Student student;
}
