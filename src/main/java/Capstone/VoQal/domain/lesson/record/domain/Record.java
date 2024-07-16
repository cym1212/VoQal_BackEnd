package Capstone.VoQal.domain.lesson.record.domain;

import Capstone.VoQal.domain.lesson.note.domain.LessonNote;
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
@Table(name = "record")
public class Record extends BaseEntity {

    @Column(nullable = false)
    private String recordUrl;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_note_id")
    private LessonNote lessonNote;
}
