package Capstone.VoQal.domain.member.repository;

import Capstone.VoQal.domain.member.domain.Coach;
import Capstone.VoQal.domain.member.domain.CoachAndStudent;
import Capstone.VoQal.domain.member.domain.Student;
import Capstone.VoQal.global.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CoachAndStudentRepository extends JpaRepository<CoachAndStudent, Long> {
    Optional<CoachAndStudent> findByCoach_IdAndStudent_Id(Long coachId, Long studentId);

    List<CoachAndStudent> findByCoachAndStatus(Coach coach, RequestStatus status);



}
