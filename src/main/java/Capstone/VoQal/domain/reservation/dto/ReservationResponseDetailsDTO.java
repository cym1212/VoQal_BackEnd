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
public class ReservationResponseDetailsDTO {
    private Long roomId;
    private Long reservationId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
