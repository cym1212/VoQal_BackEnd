package Capstone.VoQal.domain.member.repository.CoachAndStudent;

import Capstone.VoQal.domain.member.domain.CoachAndStudent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoachAndStudentRepository extends JpaRepository<CoachAndStudent, Long>, CoachAndStudentRepositoryCustom {

}
