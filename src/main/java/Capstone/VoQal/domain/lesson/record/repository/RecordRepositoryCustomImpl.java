package Capstone.VoQal.domain.lesson.record.repository;

import Capstone.VoQal.domain.lesson.record.domain.LessonRecord;
import Capstone.VoQal.domain.lesson.record.domain.QLessonRecord;
import Capstone.VoQal.domain.member.domain.QCoach;
import Capstone.VoQal.domain.member.domain.QCoachAndStudent;
import Capstone.VoQal.domain.member.domain.QMember;
import Capstone.VoQal.domain.member.domain.QStudent;
import Capstone.VoQal.global.enums.ErrorCode;
import Capstone.VoQal.global.enums.RequestStatus;
import Capstone.VoQal.global.error.exception.BusinessException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class RecordRepositoryCustomImpl implements RecordRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Transactional
    @Override
    public List<LessonRecord> findNonDeletedRecordByCoachIdAndStudentId(Long coachId, Long studentId) {
        QLessonRecord lessonRecord = QLessonRecord.lessonRecord;
        QCoachAndStudent coachAndStudent = QCoachAndStudent.coachAndStudent;
        QCoach coach = QCoach.coach;
        QStudent student = QStudent.student;
        QMember coachMember = new QMember("coachMember");
        QMember studentMember = new QMember("studentMember");

        return queryFactory.selectFrom(lessonRecord)
                .join(lessonRecord.coachAndStudent, coachAndStudent).fetchJoin()
                .join(coachAndStudent.coach, coach).fetchJoin()
                .join(coach.member, coachMember).fetchJoin()
                .join(coachAndStudent.student, student).fetchJoin()
                .join(student.member, studentMember).fetchJoin()
                .where(
                        coachMember.id.eq(coachId)
                                .and(studentMember.id.eq(studentId))
                                .and(lessonRecord.deletedAt.isNull())
                                .and(coachAndStudent.status.eq(RequestStatus.APPROVED))
                )
                .fetch();
    }
    @Override
    public LessonRecord updateLessonRecord(Long recordId, LocalDate recordDate, String recordTitle, String recordUrl) {
        QLessonRecord lessonRecord = QLessonRecord.lessonRecord;

        long affectedRows = queryFactory.update(lessonRecord)
                .where(lessonRecord.id.eq(recordId))
                .set(lessonRecord.recordDate, recordDate)
                .set(lessonRecord.recordTitle, recordTitle)
                .set(lessonRecord.recordUrl, recordUrl)
                .execute();

        if (affectedRows == 0) {
            throw new BusinessException(ErrorCode.REQUEST_FAILED);
        }

        return queryFactory.selectFrom(lessonRecord)
                .where(lessonRecord.id.eq(recordId))
                .fetchOne();
    }

    @Override
    public void deleteRecord(Long recordId) {
        QLessonRecord lessonRecord = QLessonRecord.lessonRecord;

        LocalDateTime now = LocalDateTime.now();
        long affectedRows = queryFactory.update(lessonRecord)
                .where(lessonRecord.id.eq(recordId))
                .set(lessonRecord.deletedAt, now)
                .execute();

        if (affectedRows == 0) {
            throw new BusinessException(ErrorCode.REQUEST_FAILED);
        }
    }
}
