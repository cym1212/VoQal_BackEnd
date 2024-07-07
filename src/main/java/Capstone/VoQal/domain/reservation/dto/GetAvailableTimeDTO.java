package Capstone.VoQal.domain.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAvailableTimeDTO {

    private Long roomId;
    private LocalDate requestDate;
}
