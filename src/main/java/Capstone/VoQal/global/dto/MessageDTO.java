package Capstone.VoQal.global.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
public class MessageDTO {
    @NotNull
    private final String message;

    private final int status;
}
