package Capstone.VoQal.domain.lesson.record.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadRequestDTO {
    private Long studentId;
    private LocalDate recordDate;
    private String recordTitle;

}
