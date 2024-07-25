package Capstone.VoQal.domain.lesson.url.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLessonSongUrlDTO {

        private String lessonSongUrl;
        private String singer;
        private String songTitle;
}
