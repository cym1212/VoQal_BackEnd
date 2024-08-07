package Capstone.VoQal.domain.reservation.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponseDTO {
    private int status;
    private Long roomId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
