package Capstone.VoQal.global.auth.service;


import Capstone.VoQal.domain.member.domain.Member;
import Capstone.VoQal.domain.member.repository.Member.MemberRepository;
import Capstone.VoQal.domain.member.service.MemberService;
import Capstone.VoQal.global.auth.dto.*;
import Capstone.VoQal.global.enums.ErrorCode;
import Capstone.VoQal.global.error.exception.BusinessException;

import Capstone.VoQal.global.firebase.service.FirebaseService;
import Capstone.VoQal.global.jwt.service.JwtProvider;
import com.google.firebase.auth.FirebaseAuthException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

import static Capstone.VoQal.global.enums.Role.GUEST;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;
    private final FirebaseService firebaseService;


    @Transactional
    public SignUpResponseDTO signUp(SignUpRequestDTO signUpRequestDTO) {
        Optional<Member> findEmail = memberRepository.findByEmail(signUpRequestDTO.getEmail());
        if (findEmail.isPresent()) {
            throw new BusinessException(ErrorCode.MEMBER_PROFILE_DUPLICATION);
        }

        String hashedPassword = passwordEncoder.encode(signUpRequestDTO.getPassword());
        Member member = Member.builder()
                .name(signUpRequestDTO.getName())
                .email(signUpRequestDTO.getEmail())
                .phoneNumber(signUpRequestDTO.getPhoneNum())
                .nickName(signUpRequestDTO.getNickName())
                .password(hashedPassword)
                .build();
        member.setRole(GUEST);

        memberRepository.save(member);
        return SignUpResponseDTO.builder()
                .name(member.getName())
                .email(member.getEmail())
                .phoneNum(member.getPhoneNumber())
                .nickName(member.getNickName())
                .status(200)
                .build();
    }


    @Transactional
    public GeneratedTokenDTO login(LoginRequestDTO loginRequestDTO) {
        Optional<Member> findEmail = memberRepository.findByEmail(loginRequestDTO.getEmail());

        if (findEmail.isPresent()) {
            Member member = findEmail.get();
            if (passwordEncoder.matches(loginRequestDTO.getPassword(), member.getPassword())) {
                SecurityMemberDTO securityMemberDTO = SecurityMemberDTO.builder()
                        .id(member.getId())
                        .email(member.getEmail())
                        .role(member.getRole())
                        .name(member.getName())
                        .phoneNum(member.getPhoneNumber())
                        .nickName(member.getNickName())
                        .build();

                return jwtProvider.generateTokens(securityMemberDTO);
            } else {
                throw new BusinessException(ErrorCode.INVALID_PASSWORD);
            }

        } else {
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
        }
    }

    @Transactional
    public boolean dupliacteNickname(String requestNickname) {
        Optional<Member> findNickname = memberRepository.findByNickName(requestNickname);
        return findNickname.isPresent();

    }

    @Transactional
    public boolean duplicateEmail(String requsetEmail) {
        Optional<Member> findEmail = memberRepository.findByEmail(requsetEmail);

        return findEmail.isPresent();

    }

    @Transactional
    public FindEmailResponseDTO findEmail(String name, String phoneNumber) {
        Optional<Member> findMember = memberRepository.findByNameAndPhoneNumber(name, phoneNumber);

        if (findMember.isPresent()) {
            Member findEmailMember = findMember.get();
            return FindEmailResponseDTO.builder()
                    .email(findEmailMember.getEmail())
                    .status(200)
                    .build();
        } else {
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
        }

    }

    @Transactional
    public boolean checkMember(FindRequestDTO.Password findPasswordRequestDTO) {
        Optional<Member> findMember = memberRepository.findByNameAndPhoneNumberAndEmail(findPasswordRequestDTO.getName(), findPasswordRequestDTO.getPhoneNumber(), findPasswordRequestDTO.getEmail());

        return findMember.isPresent();
    }

    @Transactional
    public void resetPassword(FindRequestDTO.ResetPassword passwordRequestDTO) {
        Optional<Member> findEmail = memberRepository.findByEmail(passwordRequestDTO.getEmail());

        findEmail.ifPresent(member -> {
            String hashedPassword = passwordEncoder.encode(passwordRequestDTO.getPassword());
            member.changePassword(hashedPassword);
            memberRepository.save(member);
        });
        if (findEmail.isEmpty()) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
        }
    }


    @Transactional
    public void deleteMember() throws ExecutionException, InterruptedException {
        Long memberId = memberService.getCurrentMember().getId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_MEMBER_ID));

        memberService.deleteMember();
    }


    @Transactional
    public void logout() {
        Long currentMember = memberService.getCurrentMember().getId();

        memberRepository.invalidateRefreshToken(currentMember);
    }

    @Transactional
    // Firebase Custom Token 생성 로직
    public CustomTokenDTO generateFirebaseCustomToken(String jwtToken) throws FirebaseAuthException {
        try {
            Claims claims = jwtProvider.verifyToken(jwtToken);

            String userId = claims.getId();


            String customToken = firebaseService.createFirebaseCustomToken(userId);
            CustomTokenDTO customTokenDTO = CustomTokenDTO.builder()
                    .firebaseCustomToken(customToken)
                    .status(200)
                    .build();
            return customTokenDTO;
        } catch (JwtException e) {
            throw new BusinessException(ErrorCode.INVALID_JWT_TOKEN);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.FAIL_TO_MAKE_FIREBASE_CUSTOM_TOKEN);
        }

    }
}



