package Capstone.VoQal.domain.lesson.record.domain;

import Capstone.VoQal.domain.lesson.note.domain.LessonNote;
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
@Table(name = "lesson_record")
public class LessonRecord extends BaseEntity {

    @Column(nullable = false)
    private String recordUrl;

    @Column(nullable = false)
    private LocalDate recordDate;

    @Column(nullable = false)
    private String recordTitle;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_note_id")
    private LessonNote lessonNote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coach_and_student_id")
    private CoachAndStudent coachAndStudent;
}
