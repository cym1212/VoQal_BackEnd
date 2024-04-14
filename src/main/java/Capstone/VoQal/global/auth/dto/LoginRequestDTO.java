package Capstone.VoQal.global.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
public class LoginRequestDTO {

    @NotBlank
    @Email(message = "잘못된 이메일 형식입니다")
    private final String email;

    @NotBlank
    private final String password;


}
