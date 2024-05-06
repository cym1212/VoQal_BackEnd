package Capstone.VoQal.global.auth.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(force = true)
public class RoleDTO {

    @Getter
    public static class CoachDTO {
        @NotNull
        @Email
        @JsonProperty("email")
        private  String email;
    }

    @Getter
    public static class StudentDTO{


        @NotNull
        private Long studentId;
    }



}
