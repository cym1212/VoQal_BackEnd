package Capstone.VoQal.domain.lesson.note.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLessonNoteDTO {


    private String updateSongTitle;

    private String updateLessonNoteTitle;

    private String updateContents;

    private String updateSinger;

    private LocalDate updateLessonDate;
}
