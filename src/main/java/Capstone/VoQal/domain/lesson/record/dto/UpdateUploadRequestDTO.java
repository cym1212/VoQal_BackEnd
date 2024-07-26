package Capstone.VoQal.domain.lesson.record.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUploadRequestDTO {

    private LocalDate updateRecordDate;
    private String updateRecordTitle;


}
