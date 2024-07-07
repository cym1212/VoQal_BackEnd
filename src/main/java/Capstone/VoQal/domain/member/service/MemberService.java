package Capstone.VoQal.domain.member.service;

import Capstone.VoQal.domain.member.domain.Coach;
import Capstone.VoQal.domain.member.domain.CoachAndStudent;
import Capstone.VoQal.domain.member.domain.Member;
import Capstone.VoQal.domain.member.domain.Student;
import Capstone.VoQal.domain.member.repository.CoachAndStudent.CoachAndStudentRepository;
import Capstone.VoQal.domain.member.repository.Member.MemberRepository;
import Capstone.VoQal.global.jwt.service.JwtTokenIdDecoder;
import Capstone.VoQal.global.enums.ErrorCode;
import Capstone.VoQal.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final JwtTokenIdDecoder jwtTokenIdDecoder;
    private final MemberRepository memberRepository;
    private final CoachAndStudentRepository coachAndStudentRepository;

    public Coach getCurrentCoach() {
        Member coachMember = getCurrentMember();
        Coach coach = coachMember.getCoach();
        if (coach == null) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
        }
        return coach;
    }

    public Member getCurrentMember() {
        long memberId = jwtTokenIdDecoder.getCurrentUserId();
        return getMemberById(memberId);
    }

    public Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_MEMBER_ID));
    }

    public CoachAndStudent getCoachAndStudent(Long coachId, Long studentId) {
        return coachAndStudentRepository.findByCoachIdAndStudentId(coachId, studentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REQUEST));
    }

    public void validateStudentEntity(Student student) {
        if (student == null) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
        }
    }
}
