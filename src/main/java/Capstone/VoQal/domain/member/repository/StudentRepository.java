package Capstone.VoQal.domain.member.repository;

import Capstone.VoQal.domain.member.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
