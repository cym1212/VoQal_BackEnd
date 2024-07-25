package Capstone.VoQal.domain.lesson.url.dto;


import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonSongResponseDTO {
    private int status;
    private String lessonSongUrl;
    private String singer;
    private String songTitle;

}
