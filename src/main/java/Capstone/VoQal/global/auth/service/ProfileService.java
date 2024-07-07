package Capstone.VoQal.global.auth.service;

import Capstone.VoQal.domain.member.service.MemberService;
import Capstone.VoQal.domain.member.domain.Coach;
import Capstone.VoQal.domain.member.domain.CoachAndStudent;
import Capstone.VoQal.domain.member.domain.Member;
import Capstone.VoQal.domain.member.domain.Student;
import Capstone.VoQal.domain.member.repository.CoachAndStudent.CoachAndStudentRepository;
import Capstone.VoQal.domain.member.repository.Coach.CoachRepository;
import Capstone.VoQal.domain.member.repository.Student.StudentRepository;
import Capstone.VoQal.global.auth.dto.ChangeNicknameDTO;
import Capstone.VoQal.domain.member.repository.Member.MemberRepository;
import Capstone.VoQal.global.auth.dto.GeneratedTokenDTO;
import Capstone.VoQal.global.auth.dto.MemberListDTO;
import Capstone.VoQal.global.auth.dto.RequestStudentListDTO;
import Capstone.VoQal.global.dto.MessageDTO;
import Capstone.VoQal.global.enums.ErrorCode;
import Capstone.VoQal.global.enums.RequestStatus;
import Capstone.VoQal.global.enums.Role;
import Capstone.VoQal.global.error.exception.BusinessException;
import Capstone.VoQal.global.jwt.service.JwtProvider;
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
    private final StudentRepository studentRepository;
    private final CoachAndStudentRepository coachAndStudentRepository;
    private final AuthService authService;
    private final JwtProvider jwtProvider;
    private final MemberService memberService;

    @Transactional
    public GeneratedTokenDTO setRoleToCoach() {
        Member member = memberService.getCurrentMember();
        Coach coach = Coach.builder()
                .member(member)
                .build();

        member.setCoach(coach);
        member.setRole(Role.COACH);

        coachRepository.save(coach);
        memberRepository.save(member);
        return reissueMemberToken(member);
    }

    private GeneratedTokenDTO reissueMemberToken(Member member) {
        String currentRefreshToken = member.getRefreshToken();
        if (currentRefreshToken == null) {
            throw new BusinessException(ErrorCode.MISMATCH_REFRESH_TOKEN);
        }

        return jwtProvider.reissueToken(currentRefreshToken);
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
        Member member = memberService.getMemberById(id);
        if (authService.dupliacteNickname(changeNicknameDTO.getNickname())) {
            throw new BusinessException(ErrorCode.NICKNAME_DUPLICATE);
        }
        member.setNickName(changeNicknameDTO.getNickname());
        memberRepository.save(member);
    }



    public List<RequestStudentListDTO> getRequestStudentList() {
        Coach coach = memberService.getCurrentCoach();
        List<CoachAndStudent> coachAndStudentList = coachAndStudentRepository.findByCoachIdAndStatus(coach.getId(), RequestStatus.PENDING);
        List<RequestStudentListDTO> requestStudentList = new ArrayList<>();

        for (CoachAndStudent coachAndStudent : coachAndStudentList) {
            Student student = coachAndStudent.getStudent();
            if (student != null) {
                Member studentMember = student.getMember();
                RequestStudentListDTO dto = new RequestStudentListDTO(studentMember.getId(), studentMember.getName());
                requestStudentList.add(dto);
            }
        }

        return requestStudentList;
    }

    @Transactional
    public void requestCoach(Long coachMemberId) {
        Member studentMember = memberService.getCurrentMember();
        Student student = studentMember.getStudent();

        if (student == null) {
            student = Student.builder()
                    .member(studentMember)
                    .build();
            studentMember.setStudent(student);
            student = studentRepository.save(student);
        }

        Member coachMember = memberService.getMemberById(coachMemberId);
        Coach coach = coachMember.getCoach();

        if (coach == null) {
            coach = Coach.builder()
                    .member(coachMember)
                    .build();
            coachMember.setCoach(coach);
            coach = coachRepository.save(coach);
        }


        List<CoachAndStudent> existingRequests = coachAndStudentRepository.findByStudentId(student.getId());
        boolean hasPendingOrApproved = existingRequests.stream()
                .anyMatch(rel -> rel.getStatus() == RequestStatus.PENDING || rel.getStatus() == RequestStatus.APPROVED);

        if (hasPendingOrApproved) {
            throw new BusinessException(ErrorCode.WRONG_STATUS);
        }

        CoachAndStudent coachAndStudent = CoachAndStudent.builder()
                .coach(coach)
                .student(student)
                .status(RequestStatus.PENDING)
                .build();

        coachAndStudentRepository.save(coachAndStudent);
    }

    @Transactional
    public GeneratedTokenDTO approveRequest(Long studentMemberId) {
        Coach coach = memberService.getCurrentCoach();
        Member studentMember = memberService.getMemberById(studentMemberId);
        Student student = studentMember.getStudent();
        memberService.validateStudentEntity(student);

        CoachAndStudent coachAndStudent = memberService.getCoachAndStudent(coach.getId(), student.getId());
        if (coachAndStudent.getStatus() != RequestStatus.PENDING) {
            throw new IllegalArgumentException("Invalid request status");
        }
        coachAndStudent.setStatus(RequestStatus.APPROVED);

        studentMember.setRole(Role.STUDENT);
        memberRepository.save(studentMember);
        coachAndStudentRepository.save(coachAndStudent);
        return reissueMemberToken(studentMember);

    }

    @Transactional
    public void rejectRequest(Long requestId) {
        Coach coach = memberService.getCurrentCoach();
        Member studentMember = memberService.getMemberById(requestId);
        Student student = studentMember.getStudent();
        memberService.validateStudentEntity(student);

        CoachAndStudent coachAndStudent = memberService.getCoachAndStudent(coach.getId(), student.getId());
        if (coachAndStudent.getStatus() != RequestStatus.PENDING) {
            throw new IllegalArgumentException("Invalid request status");
        }
        coachAndStudent.setStatus(RequestStatus.REJECTED);

        coachAndStudentRepository.save(coachAndStudent);
    }

    public List<MemberListDTO> getStudentList() {
        Member coach = memberService.getCurrentMember();
        List<CoachAndStudent> approveStudent = coachAndStudentRepository.findApprovedStudentsByCoachId(coach.getId());
        List<MemberListDTO> studentList = new ArrayList<>();
        for (CoachAndStudent coachAndStudent : approveStudent) {
            studentList.add(new MemberListDTO(coachAndStudent.getStudent().getId(),
                    coachAndStudent.getStudent().getMember().getName()));
        }
        return studentList;
    }


    @Transactional
    public MessageDTO deleteStudent(Long studentId) {
        Member coach = memberService.getCurrentMember();
        coachAndStudentRepository.deleteByCoachIdAndStudentId(coach.getId(), studentId);
        Member member = memberService.getMemberById(studentId);
        member.setRole(Role.GUEST);
        MessageDTO messageDTO = MessageDTO.builder()
                .status(200)
                .message("성공적으로 삭제되었습니다")
                .build();
        return messageDTO;
    }

    //todo
    // 로직 테스트 다시하기



}
