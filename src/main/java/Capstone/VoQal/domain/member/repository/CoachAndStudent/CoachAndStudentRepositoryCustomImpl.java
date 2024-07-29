package Capstone.VoQal.domain.member.repository.CoachAndStudent;

import Capstone.VoQal.domain.member.domain.*;
import Capstone.VoQal.global.enums.RequestStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CoachAndStudentRepositoryCustomImpl implements CoachAndStudentRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public Optional<CoachAndStudent> findByCoachIdAndStudentId(Long coachId, Long studentId) {
        QCoachAndStudent coachAndStudent = QCoachAndStudent.coachAndStudent;

        CoachAndStudent foundCoachAndStudent = queryFactory.selectFrom(coachAndStudent)
                .where(coachAndStudent.coach.member.id.eq(coachId)
                        .and(coachAndStudent.student.member.id.eq(studentId))
                        .and(coachAndStudent.status.eq(RequestStatus.APPROVED)))
                .fetchOne();

        return Optional.ofNullable(foundCoachAndStudent);
    }
    @Override
    public Optional<CoachAndStudent> findByCoachIdAndStudentIdWithPendingStatus(Long coachId, Long studentId) {
        QCoachAndStudent coachAndStudent = QCoachAndStudent.coachAndStudent;

        CoachAndStudent foundCoachAndStudent = queryFactory.selectFrom(coachAndStudent)
                .where(coachAndStudent.coach.member.id.eq(coachId)
                        .and(coachAndStudent.student.member.id.eq(studentId))
                        .and(coachAndStudent.status.eq(RequestStatus.PENDING)))
                .fetchOne();

        return Optional.ofNullable(foundCoachAndStudent);
    }

    @Override
    public List<CoachAndStudent> findByStudentId(Long studentId) {
        QCoachAndStudent coachAndStudent = QCoachAndStudent.coachAndStudent;

        return queryFactory.selectFrom(coachAndStudent)
                .where(coachAndStudent.student.member.id.eq(studentId))
                .fetch();
    }

    @Override
    public List<CoachAndStudent> findByCoachIdAndStatus(Long coachId, RequestStatus status) {
        QCoachAndStudent coachAndStudent = QCoachAndStudent.coachAndStudent;

        return queryFactory.selectFrom(coachAndStudent)
                .where(coachAndStudent.coach.member.id.eq(coachId)
                        .and(coachAndStudent.status.eq(status)))
                .fetch();
    }

    @Override
    public List<CoachAndStudent> findApprovedStudentsByCoachId(Long coachMemberId) {
        QCoachAndStudent coachAndStudent = QCoachAndStudent.coachAndStudent;
        QStudent student = QStudent.student;
        QMember member = QMember.member;

        return queryFactory.selectFrom(coachAndStudent)
                .join(coachAndStudent.student, student).fetchJoin()
                .join(student.member, member).fetchJoin()
                .where(coachAndStudent.coach.member.id.eq(coachMemberId)
                        .and(coachAndStudent.status.eq(RequestStatus.APPROVED)))
                .fetch();
    }

    @Override
    @Transactional
    public void deleteByCoachIdAndStudentId(Long coachId, Long studentId) {
        QCoachAndStudent coachAndStudent = QCoachAndStudent.coachAndStudent;

        long deletedCount = queryFactory.delete(coachAndStudent)
                .where(coachAndStudent.coach.member.id.eq(coachId)
                        .and(coachAndStudent.student.member.id.eq(studentId)))
                .execute();
    }

    @Transactional
    public Optional<RequestStatus> getStudentStatusByMemberId(Long memberId) {
        QMember qMember = QMember.member;
        QStudent qStudent = QStudent.student;
        QCoachAndStudent qCoachAndStudent = QCoachAndStudent.coachAndStudent;

        RequestStatus status = queryFactory
                .select(qCoachAndStudent.status)
                .from(qCoachAndStudent)
                .join(qCoachAndStudent.student, qStudent)
                .join(qStudent.member, qMember)
                .where(qMember.id.eq(memberId))
                .fetchFirst();

        return Optional.ofNullable(status);
    }

    @Transactional
    public Long findCoachIdByStudentId(Long studentId) {
        QCoachAndStudent coachAndStudent = QCoachAndStudent.coachAndStudent;
        QStudent student = QStudent.student;
        QMember studentMember = new QMember("studentMember");

        return queryFactory.select(coachAndStudent.coach.member.id)
                .from(coachAndStudent)
                .join(coachAndStudent.student, student)
                .join(student.member, studentMember)
                .where(
                        studentMember.id.eq(studentId)
                                .and(coachAndStudent.status.eq(RequestStatus.APPROVED))
                )
                .fetchOne();
    }

}
