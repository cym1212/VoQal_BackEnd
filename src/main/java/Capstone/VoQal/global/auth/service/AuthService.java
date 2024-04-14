package Capstone.VoQal.global.auth.service;


import Capstone.VoQal.domain.member.domain.Member;
import Capstone.VoQal.domain.member.repository.MemberRepository;
import Capstone.VoQal.global.auth.dto.SecurityMemberDTO;
import Capstone.VoQal.global.auth.dto.GeneratedTokenDTO;
import Capstone.VoQal.global.auth.dto.SignUpRequestDTO;
import Capstone.VoQal.global.auth.dto.SignUpResponseDTO;
import Capstone.VoQal.global.enums.ErrorCode;
import Capstone.VoQal.global.enums.Role;
import Capstone.VoQal.global.error.exception.BusinessException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

import static Capstone.VoQal.global.enums.Role.GUEST;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public SignUpResponseDTO signUp(SignUpRequestDTO signUpRequestDTO) {
        Optional<Member> findEmail = memberRepository.findByEmail(signUpRequestDTO.getEmail());
        if (findEmail.isPresent()) {
            throw new BusinessException(ErrorCode.MEMBER_PROFILE_DUPLICATION);
        }
        if (signUpRequestDTO.getEmail().isEmpty() && signUpRequestDTO.getName().isEmpty() && signUpRequestDTO.getPassword().isEmpty() && signUpRequestDTO.getNickName().isEmpty() && signUpRequestDTO.getPhoneNum().isEmpty()) {
            throw new BusinessException(ErrorCode.INCOMPLETE_SIGNUP_INFO);
        }
        Role role = GUEST;

        String hashedPassword = passwordEncoder.encode(signUpRequestDTO.getPassword());
        Member member = Member.builder()
                .name(signUpRequestDTO.getName())
                .email(signUpRequestDTO.getEmail())
                .phoneNumber(signUpRequestDTO.getPhoneNum())
                .nickName(signUpRequestDTO.getNickName())
                .password(hashedPassword)
                .build();
        member.setRole(role);

        memberRepository.save(member);
        return SignUpResponseDTO.builder()
                .name(member.getName())
                .email(member.getEmail())
                .phoneNum(member.getPhoneNumber())
                .nickName(member.getNickName())
                .build();
    }



    @Transactional
    public GeneratedTokenDTO login(String email, String password) {
        Optional<Member> findEmail = memberRepository.findByEmail(email);

        if (findEmail.isPresent()) {
            Member member = findEmail.get();
            if (passwordEncoder.matches(password, member.getPassword())) {
                SecurityMemberDTO securityMemberDTO = SecurityMemberDTO.builder()
                        .id(member.getId())
                        .email(member.getEmail())
                        .role(member.getRole())
                        .name(member.getName())
                        .phoneNum(member.getPhoneNumber())
                        .nickName(member.getNickName())
                        .build();

                GeneratedTokenDTO generatedTokenDTO = jwtProvider.generateTokens(securityMemberDTO);

                return generatedTokenDTO;
            } else {
                throw new BusinessException(ErrorCode.INVALID_PASSWORD);
            }

        } else {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }
}



