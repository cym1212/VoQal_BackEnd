package Capstone.VoQal.global.auth.controller;

import Capstone.VoQal.global.auth.dto.GeneratedTokenDTO;
import Capstone.VoQal.global.auth.dto.LoginRequestDTO;
import Capstone.VoQal.global.auth.dto.SignUpRequestDTO;
import Capstone.VoQal.global.auth.dto.SignUpResponseDTO;
import Capstone.VoQal.global.auth.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDTO> signUp(@RequestBody @Valid SignUpRequestDTO signUpRequestDTO) {

        SignUpResponseDTO responseDTO = authService.signUp(signUpRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<GeneratedTokenDTO> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        String email = String.valueOf(loginRequestDTO.getEmail());
        String password = String.valueOf(loginRequestDTO.getPassword());
        GeneratedTokenDTO generatedTokenDTO = authService.login(email, password);
        return ResponseEntity.ok(generatedTokenDTO);

    }

    @PostMapping("/test")
    public String test() {
        return "sucess";
    }
}
