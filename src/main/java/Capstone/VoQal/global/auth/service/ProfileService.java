package Capstone.VoQal.global.auth.service;

import Capstone.VoQal.domain.member.domain.Coach;
import Capstone.VoQal.domain.member.domain.CoachAndStudent;
import Capstone.VoQal.domain.member.domain.Member;
import Capstone.VoQal.domain.member.domain.Student;
import Capstone.VoQal.domain.member.repository.CoachAndStudentRepository;
import Capstone.VoQal.domain.member.repository.CoachRepository;
import Capstone.VoQal.domain.member.repository.StudentRepository;
import Capstone.VoQal.global.auth.dto.ChangeNicknameDTO;
import Capstone.VoQal.domain.member.repository.MemberRepository;
import Capstone.VoQal.global.auth.dto.MemberListDTO;
import Capstone.VoQal.global.auth.dto.RequestStudentListDTO;
import Capstone.VoQal.global.enums.ErrorCode;
import Capstone.VoQal.global.enums.RequestStatus;
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
    private final StudentRepository studentRepository;
    private final CoachAndStudentRepository coachAndStudentRepository;
    private final AuthService authService;
    private final JwtTokenIdDecoder jwtTokenIdDecoder;

    @Transactional
    public void setRoleToCoach() {
        long id = jwtTokenIdDecoder.getCurrentUserId();
        Optional<Member> findCoachId = memberRepository.findByMemberId(id);
        if (findCoachId.isEmpty()) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
        }
        findCoachId.ifPresent(member -> {
            Coach coach = Coach.builder()
                    .member(member)
                    .build();

            member.setCoach(coach);
            member.setRole(Role.COACH);

            coachRepository.save(coach);
            memberRepository.save(member);
        });

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

    public void requestCoach(Long studentMemberId, Long coachMemberId) {
        Member studentMember = memberRepository.findById(studentMemberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student ID"));
        Student student = studentMember.getStudent();

        if (student == null) {
            student = Student.builder()
                    .member(studentMember)
                    .build();
            studentMember.setStudent(student);
            student = studentRepository.save(student);
        }

        Member coachMember = memberRepository.findById(coachMemberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid coach ID"));
        Coach coach = coachMember.getCoach();

        CoachAndStudent coachAndStudent = CoachAndStudent.builder()
                .coach(coach)
                .student(student)
                .status(RequestStatus.PENDING)
                .build();

        System.out.println("student = " + student);
        System.out.println("coach = " + coach);
        coachAndStudentRepository.save(coachAndStudent);
    }

    public List<RequestStudentListDTO> getRequestStudentList() {
        Coach coach = getCoach();
        List<CoachAndStudent> coachAndStudentList = coachAndStudentRepository.findByCoachAndStatus(coach ,RequestStatus.PENDING);
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


    public void approveRequest(Long studentMemberId) {
        Coach coach = getCoach();

        Member studentMember = memberRepository.findById(studentMemberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student member ID"));
        Student student = studentMember.getStudent();
        if (student == null) {
            throw new IllegalArgumentException("No Student entity found for the given member ID");
        }

        CoachAndStudent coachAndStudent = coachAndStudentRepository.findByCoach_IdAndStudent_Id(coach.getId(), student.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid request or the request does not exist"));

        coachAndStudent.setStatus(RequestStatus.APPROVED);

        studentMember.setRole(Role.STUDENT);
        memberRepository.save(studentMember);

        coachAndStudentRepository.save(coachAndStudent);
    }

    public void rejectRequest(Long requestId) {
        Coach coach = getCoach();
        Member studentMember = memberRepository.findById(requestId).orElseThrow(() -> new IllegalArgumentException("Invalid student member ID"));
        Student student = studentMember.getStudent();
        if (student == null) {
            throw new IllegalArgumentException("No Student entity found for the given member ID");
        }
        CoachAndStudent coachAndStudent = coachAndStudentRepository.findByCoach_IdAndStudent_Id(coach.getId(), student.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid request or the request does not exist"));

        coachAndStudent.setStatus(RequestStatus.REJECTED);
        coachAndStudentRepository.save(coachAndStudent);
    }

    private Coach getCoach() {
        long coachMemberId = jwtTokenIdDecoder.getCurrentUserId();
        Member coachMember = memberRepository.findById(coachMemberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid coach ID"));

        Coach coach = coachMember.getCoach();
        if (coach == null) {
            throw new IllegalArgumentException("No Coach entity found for the given member ID");
        }
        return coach;
    }

}