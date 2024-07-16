package Capstone.VoQal.global.auth.dto;


import Capstone.VoQal.global.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentStatusDTO {

    private RequestStatus status;
}
