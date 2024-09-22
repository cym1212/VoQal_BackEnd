package Capstone.VoQal.domain.member.service;

import Capstone.VoQal.domain.member.domain.CoachAndStudent;
import Capstone.VoQal.domain.member.domain.Member;
import Capstone.VoQal.domain.member.domain.Student;

import java.util.concurrent.ExecutionException;

public interface MemberService {

    Member getCurrentCoach();

    Member getStudent(Long studentId);

    Member getCurrentMember();


    Member getMemberById(Long memberId);

    CoachAndStudent getCoachAndStudent(Long coachId, Long studentId);

    void validateStudentEntity(Student student);

    Long getCoachIdByStudentId(Long studentId);

    CoachAndStudent getCoachAndStudentWithSignUp(Long coachId, Long studentId);

    public void deleteMember() throws ExecutionException, InterruptedException;
}
