package Capstone.VoQal.domain.reservation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequestDTO {
    private Long roomId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}