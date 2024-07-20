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
public class LessonNoteRequestDTO {
    private Long studentId;

    private String songTitle;

    private String lessonNoteTitle;

    private String contents;

    private String singer;

    private LocalDate lessonDate;
}
