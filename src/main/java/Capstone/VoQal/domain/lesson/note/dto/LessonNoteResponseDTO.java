package Capstone.VoQal.domain.lesson.note.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonNoteResponseDTO {
    private Long lessonNoteId;

    private String lessonNoteTitle;

    private String songTitle;

    private String singer;

    private LocalDate lessonDate;
}
