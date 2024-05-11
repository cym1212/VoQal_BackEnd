package Capstone.VoQal.global.auth.service;

import Capstone.VoQal.domain.member.domain.Coach;
import Capstone.VoQal.domain.member.domain.Member;
import Capstone.VoQal.domain.member.repository.CoachRepository;
import Capstone.VoQal.global.auth.dto.ChangeNicknameDTO;
import Capstone.VoQal.domain.member.repository.MemberRepository;
import Capstone.VoQal.global.auth.dto.MemberListDTO;
import Capstone.VoQal.global.enums.ErrorCode;
import Capstone.VoQal.global.enums.Role;
import Capstone.VoQal.global.error.exception.BusinessException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {
    private final MemberRepository memberRepository;
    private final CoachRepository coachRepository;
    private final AuthService authService;
    private final JwtTokenIdDecoder jwtTokenIdDecoder;

    @Transactional
    public void setRoleToCoach() {
        long id = jwtTokenIdDecoder.extractIdFromTokenInHeader();
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

    @Transactional
    public void requestCoachAssignment(Long coachId) {
        Long studentId = jwtTokenIdDecoder.extractIdFromTokenInHeader();

        Member studentMember = memberRepository.findById(studentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        Member coachMember = memberRepository.findById(coachId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        if (coachMember.getRole() != Role.COACH) {
            throw new BusinessException(ErrorCode.INVALID_ROLE);
        }

        Coach coach = coachRepository.findByMemberId(coachId);
        if (coach == null) {
            coach = new Coach();
            coach.setMember(coachMember);
        }
        coach.addPendingStudentId(studentId);
        coachRepository.save(coach);
    }

    @Transactional
    public List<MemberListDTO> requestedStudentList() {
        Long coachId = jwtTokenIdDecoder.extractIdFromTokenInHeader();
        Member coachMember = memberRepository.findById(coachId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        Coach coach = coachMember.getCoach();

        if (coach == null) {
            throw new BusinessException(ErrorCode.COACH_NOT_FOUND);
        }

        List<MemberListDTO> studentList = new ArrayList<>();

        coach.initPendingStudentIds();

        List<Long> pendingStudentId = new ArrayList<>(coach.getPendingStudentIds());

        if (!pendingStudentId.isEmpty()) {
            List<Member> students = memberRepository.findAllByIdIn(pendingStudentId);

            for (Member student : students) {
                MemberListDTO studentDTO = MemberListDTO.builder()
                        .id(student.getId())
                        .name(student.getName())
                        .build();
                studentList.add(studentDTO);
            }
        }

        return studentList;
    }


}



