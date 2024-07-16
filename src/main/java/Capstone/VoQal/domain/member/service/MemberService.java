package Capstone.VoQal.domain.member.service;

import Capstone.VoQal.domain.member.domain.Coach;
import Capstone.VoQal.domain.member.domain.CoachAndStudent;
import Capstone.VoQal.domain.member.domain.Member;
import Capstone.VoQal.domain.member.domain.Student;

public interface MemberService {

    Coach getCurrentCoach();

    Student getCurrentStudent(Long studentId);

    Member getCurrentMember();

    Long getCurrentMemberId();

    Member getMemberById(Long memberId);

    CoachAndStudent getCoachAndStudent(Long coachId, Long studentId);

    void validateStudentEntity(Student student);
}
