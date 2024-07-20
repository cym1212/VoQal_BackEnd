package Capstone.VoQal.domain.reservation.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAvailableTimeDTO {

    private Long roomId;
    private LocalDate requestDate;
}
