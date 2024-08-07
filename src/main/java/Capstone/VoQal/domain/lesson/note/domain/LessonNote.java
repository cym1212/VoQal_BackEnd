package Capstone.VoQal.domain.lesson.note.domain;


import Capstone.VoQal.domain.lesson.record.domain.LessonRecord;
import Capstone.VoQal.domain.member.domain.CoachAndStudent;
import Capstone.VoQal.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    private String singer;

    @Column(nullable = false)
    private LocalDate lessonDate;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coach_and_student_id")
    private CoachAndStudent coachAndStudent;

    @OneToOne(mappedBy = "lessonNote", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private LessonRecord lessonRecord;
}
