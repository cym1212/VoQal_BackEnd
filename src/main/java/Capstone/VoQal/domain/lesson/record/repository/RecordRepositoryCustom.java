package Capstone.VoQal.domain.lesson.record.repository;

import Capstone.VoQal.domain.lesson.record.domain.LessonRecord;

import java.time.LocalDate;
import java.util.List;

public interface RecordRepositoryCustom {
    List<LessonRecord> findNonDeletedRecordByCoachIdAndStudentId(Long coachId, Long studentId);

    LessonRecord updateLessonRecord(Long recordId, LocalDate recordDate, String recordTitle, String recordUrl);

    void deleteRecord(Long recordId);
}

