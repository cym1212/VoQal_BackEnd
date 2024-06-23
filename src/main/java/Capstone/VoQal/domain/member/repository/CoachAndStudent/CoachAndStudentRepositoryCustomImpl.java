package Capstone.VoQal.domain.member.repository.CoachAndStudent;

import Capstone.VoQal.domain.member.domain.CoachAndStudent;
import Capstone.VoQal.domain.member.domain.Member;
import Capstone.VoQal.domain.member.domain.QCoachAndStudent;
import Capstone.VoQal.domain.member.domain.QMember;
import Capstone.VoQal.global.enums.RequestStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CoachAndStudentRepositoryCustomImpl implements CoachAndStudentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CoachAndStudentRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }
    //todo
    // 코드 분석하기
    // 그럼 코드가 어떤식으로 바뀌는지 확인
    // 관련 개념들 정리해서 블로그 쓰기
    @Override
    public Optional<Member> findMemberWithCoachAndStudentById(Long memberId) {
        QMember member = QMember.member;

        Member foundMember = queryFactory.selectFrom(member)
                .leftJoin(member.coach).fetchJoin()
                .leftJoin(member.student).fetchJoin()
                .where(member.id.eq(memberId))
                .fetchOne();

        return Optional.ofNullable(foundMember);
    }

    @Override
    public Optional<CoachAndStudent> findByCoachIdAndStudentId(Long coachId, Long studentId) {
        QCoachAndStudent coachAndStudent = QCoachAndStudent.coachAndStudent;

        CoachAndStudent foundCoachAndStudent = queryFactory.selectFrom(coachAndStudent)
                .where(coachAndStudent.coach.id.eq(coachId)
                        .and(coachAndStudent.student.id.eq(studentId)))
                .fetchOne();

        return Optional.ofNullable(foundCoachAndStudent);
    }

    @Override
    public List<CoachAndStudent> findByCoachIdAndStatus(Long coachId, RequestStatus status) {
        QCoachAndStudent coachAndStudent = QCoachAndStudent.coachAndStudent;

        return queryFactory.selectFrom(coachAndStudent)
                .where(coachAndStudent.coach.id.eq(coachId)
                        .and(coachAndStudent.status.eq(status)))
                .fetch();
    }


}