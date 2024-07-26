package Capstone.VoQal.domain.lesson.record.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadResponseDTO {

    private LocalDate recordDate;
    private String recordTitle;
    private String s3Url;
}
