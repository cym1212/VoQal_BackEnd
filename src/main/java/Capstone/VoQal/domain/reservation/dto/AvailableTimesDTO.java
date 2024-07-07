package Capstone.VoQal.domain.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailableTimesDTO {
    private Long roomId;
    private List<LocalDateTime> availableTimes;
}