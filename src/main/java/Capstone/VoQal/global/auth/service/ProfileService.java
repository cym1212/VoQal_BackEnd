package Capstone.VoQal.global.auth.service;

import Capstone.VoQal.domain.member.domain.Member;
import Capstone.VoQal.global.auth.dto.ChangeNicknameDTO;
import Capstone.VoQal.domain.member.repository.MemberRepository;
import Capstone.VoQal.global.auth.dto.MemberListDTO;
import Capstone.VoQal.global.auth.dto.RoleDTO;
import Capstone.VoQal.global.enums.ErrorCode;
import Capstone.VoQal.global.enums.Role;
import Capstone.VoQal.global.error.exception.BusinessException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {
    private final MemberRepository memberRepository;
    private final AuthService authService;
    private final JwtProvider jwtProvider;

    @Transactional
    public void setRoleToCoach() {
        long id = jwtProvider.extractIdFromTokenInHeader();
        Optional<Member> findId = memberRepository.findById(id);

        findId.ifPresent(member -> {
            member.setRole(Role.COACH);
            memberRepository.save(member);
        });
        if (findId.isEmpty()) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
        }
    }

    @Transactional
    public List<MemberListDTO> getCoachList() {
        List<Member> coachList = memberRepository.findByRole(Role.COACH);
        List<MemberListDTO> coachListDTO = new ArrayList<>();
        for (Member coach : coachList) {
            coachListDTO.add(new MemberListDTO(coach.getId(), coach.getName()));
        }
        return coachListDTO;
    }

    @Transactional
    public void updateNickname(Long id, ChangeNicknameDTO changeNicknameDTO) {
        Optional<Member> findMemberId = memberRepository.findByMemberId(id);
        if (findMemberId.isEmpty()) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
        }
        if (authService.dupliacteNickname(changeNicknameDTO.getNickname())) {
            throw new BusinessException(ErrorCode.NICKNAME_DUPLICATE);
        }
        Member member = findMemberId.get();
        member.setNickName(changeNicknameDTO.getNickname());
        memberRepository.save(member);

    }

//    @Transactional
//    public void requestCoachAssignment(Long coachId, RoleDTO.StudentDTO studentDTO) {
//        Optional<Member> findStudent = memberRepository.findByMemberId(studentDTO.getStudentId());
//        if (findStudent.isEmpty()) {
//            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
//        }
//        Member student = findStudent.get();
//
//        Optional<Member> findCoach = memberRepository.findByMemberId(coachId);
//        if (findCoach.isEmpty()) {
//            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
//        }
//        Member
//
//    }


}
