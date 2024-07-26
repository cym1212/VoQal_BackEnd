package Capstone.VoQal.domain.lesson.record.repository;

import Capstone.VoQal.domain.lesson.record.domain.LessonRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<LessonRecord,Long>, RecordRepositoryCustom {
}
