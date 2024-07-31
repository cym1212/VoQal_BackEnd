package Capstone.VoQal.global.jwt.service;

import Capstone.VoQal.global.auth.dto.SecurityMemberDTO;
import Capstone.VoQal.global.enums.ErrorCode;
import Capstone.VoQal.global.error.exception.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;



@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenIdDecoder {

    private final ObjectMapper objectMapper;
//    public Long getCurrentUserId() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.isAuthenticated()) {
//            SecurityMemberDTO userDetails = (SecurityMemberDTO) authentication.getPrincipal();
//            return userDetails.getId();
//        }
//        throw  new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
//    }


    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof SecurityMemberDTO) {
                return ((SecurityMemberDTO) principal).getId();
            } else if (principal instanceof String) {
                try {
                    SecurityMemberDTO userDetails = objectMapper.readValue((String) principal, SecurityMemberDTO.class);
                    return userDetails.getId();
                } catch (Exception e) {
                    log.error("Error parsing principal to SecurityMemberDTO", e);
                    throw new BusinessException(ErrorCode.TOKEN_ERROR);
                }
            }
        }
        throw new BusinessException(ErrorCode.TOKEN_ERROR);
    }
}
