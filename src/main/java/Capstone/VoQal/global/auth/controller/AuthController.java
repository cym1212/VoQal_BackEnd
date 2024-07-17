package Capstone.VoQal.global.auth.controller;

import Capstone.VoQal.global.auth.dto.*;
import Capstone.VoQal.global.auth.service.AuthService;

import Capstone.VoQal.global.error.exception.ErrorResponse;
import Capstone.VoQal.global.jwt.service.JwtProvider;
import Capstone.VoQal.global.dto.MessageDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final JwtProvider jwtProvider;

    @PostMapping("/signup")
    @Operation(summary = "회원가입 로직", description = "이메일, 이름, 전화번호, 닉네임, 비밀번호를 입력하면 검증 후 회원가입을 진행합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "400", description = "이미 존재하는 회원입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
    public ResponseEntity<SignUpResponseDTO> signUp(@RequestBody @Valid SignUpRequestDTO signUpRequestDTO) {

        SignUpResponseDTO responseDTO = authService.signUp(signUpRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인 로직", description = "이메일, 비밀번호를 입력하면 검증 후 로그인을 진행하고 성공하면 access Token, Refresh Token을 발급합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 비밀번호입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "400", description = "유효하지 않은 입력값입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "400", description = "토큰 관련 오류들.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
    public ResponseEntity<GeneratedTokenDTO> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {

        GeneratedTokenDTO generatedTokenDTO = authService.login(loginRequestDTO);
        return ResponseEntity.ok(generatedTokenDTO);

    }

    @PostMapping("/duplicate/nickname")
    @Operation(summary = "닉네임 중복 검사 로직", description = "닉네임이 중복되었는지 검사합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "사용 가능한 닉네임입니다."),
                    @ApiResponse(responseCode = "400", description = "닉네임이 중복되었습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
    public ResponseEntity<MessageDTO> duplicateNickname(@RequestBody DuplicateDTO.NickName duplicateNicknameDTO) {
        String requestNickname = duplicateNicknameDTO.getNickName();
        boolean result = authService.dupliacteNickname(requestNickname);
        if (result) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(MessageDTO.builder()
                            .message("닉네임이 중복되었습니다.")
                            .status(HttpStatus.BAD_REQUEST.value())
                            .build());
        } else {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(MessageDTO.builder()
                            .message("사용 가능한 닉네임입니다.")
                            .status(HttpStatus.OK.value())
                            .build());
        }
    }

    @PostMapping("/duplicate/email")
    @Operation(summary = "이메일 검증 로직", description = "이메일이 중복되었는지 검사합니다",
            responses = {
                    @ApiResponse(responseCode = "200", description = "사용 가능한 이메일입니다."),
                    @ApiResponse(responseCode = "400", description = "이미 사용중인 이메일입니다..", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
    public ResponseEntity<MessageDTO> duplicateEmail(@RequestBody DuplicateDTO.Email duplicateEmailDTO) {
        String requsetEmail = duplicateEmailDTO.getEmail();
        boolean isDuplicate = authService.duplicateEmail(requsetEmail);
        if (isDuplicate) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(MessageDTO.builder()
                            .message("이미 사용중인 이메일입니다.")
                            .status(HttpStatus.BAD_REQUEST.value())
                            .build());
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(MessageDTO.builder()
                        .message("사용 가능한 이메일 입니다.")
                        .status(HttpStatus.OK.value())
                        .build());
    }


    @PostMapping("/find/email")
    @Operation(summary = "이메일 찾기 ", description = "전화번호와 이름을 확인하여 이메일을 조회합니다. ver2에는 본인인증도 구현 예정",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "400", description = "존재하지 않는 회원입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
    public ResponseEntity<FindEmailResponseDTO> findEmail(@Valid @RequestBody FindRequestDTO findRequestDTO) {
        String name = findRequestDTO.getName();
        String phoneNumber = findRequestDTO.getPhoneNumber();
        FindEmailResponseDTO findEmailResponseDTO = authService.findEmail(name, phoneNumber);
        return ResponseEntity.ok(findEmailResponseDTO);
    }




    @PostMapping("/find/member")
    @Operation(summary = "회원인지 찾기 ", description = "이름과 전화번호 이메일을 사용해서 가입된 회원인지 확인합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원이 맞습니다"),
                    @ApiResponse(responseCode = "400", description = "일치하는 회원 정보가 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
    public ResponseEntity<MessageDTO> checkMember(@Valid @RequestBody FindRequestDTO.Password findPasswordRequestDTO) {
        boolean isChecked = authService.checkMember(findPasswordRequestDTO);
        if (isChecked) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(MessageDTO.builder()
                            .message("회원이 맞습니다.")
                            .status(HttpStatus.OK.value())
                            .build());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(MessageDTO.builder()
                        .message("일치하는 회원 정보가 없습니다")
                        .status(HttpStatus.OK.value())
                        .build());
    }

    @PostMapping("/reset/password")
    @Operation(summary = "비밀번호 재설정 로직 ", description = "이전에 검증할때 사용했던 이메일도 함께 첨부 바람, 비밀번호 재설정 로직",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "400", description = "존재하지 않는 회원입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
    public ResponseEntity<MessageDTO> resetPassword(@Valid @RequestBody FindRequestDTO.ResetPassword resetPasswordDTO) {
        authService.resetPassword(resetPasswordDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(MessageDTO.builder()
                        .message("성공적으로 변경되었습니다")
                        .status(HttpStatus.OK.value())
                        .build());

    }

    @PatchMapping("/tokens")
    @Operation(summary = "토큰 재발급", description = "Access Token과 남은 기간에 따라 Refresh Token을 재발급 합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "400", description = "존재하지 않는 회원입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "400", description = "유효하지 않은 리프레쉬 토큰입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
    public GeneratedTokenDTO tokenModify(@Valid @RequestBody TokenModifyDTO tokenModifyRequest) {
        return jwtProvider.reissueToken(tokenModifyRequest.getRefreshToken());
    }


    @PostMapping("/test")
    public String test() {
        return "sucess";
    }
}
