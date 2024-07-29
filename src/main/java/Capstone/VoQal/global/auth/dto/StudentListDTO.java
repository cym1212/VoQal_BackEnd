package Capstone.VoQal.global.auth.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class StudentListDTO {

    private Long id;
    private String name;
    private String lessonSongUrl;
    private String singer;
    private String songTitle;
}
