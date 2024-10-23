package Capstone.VoQal.domain.member.repository.Member;

import Capstone.VoQal.domain.member.domain.Member;
import Capstone.VoQal.domain.member.domain.QCoachAndStudent;
import Capstone.VoQal.domain.member.domain.QMember;
import Capstone.VoQal.global.auth.dto.MemberInfromationDTO;
import Capstone.VoQal.global.enums.ErrorCode;
import Capstone.VoQal.global.enums.Role;
import Capstone.VoQal.global.error.exception.BusinessException;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Member> findByMemberId(Long memberId) {
        QMember member = QMember.member;

        Member foundMember = queryFactory.selectFrom(member)
                .where(member.id.eq(memberId))
                .fetchOne();

        return Optional.ofNullable(foundMember);
    }

    public String findRoleById(Long memberId) {
        QMember member = QMember.member;

        return String.valueOf(queryFactory.select(member.role)
                .from(member)
                .where(member.id.eq(memberId))
                .fetchOne());
    }

    @Override
    @Transactional
    public void updateRefreshToken(Long id, String refreshToken) {
        QMember member = QMember.member;

        queryFactory.update(member)
                .set(member.refreshToken, refreshToken)
                .where(member.id.eq(id))
                .execute();
    }

    @Override
    @Transactional
    public void updateFcmToken(Long id, String fcmToken) {
        QMember member = QMember.member;

        queryFactory.update(member)
                .set(member.fcmToken, fcmToken)
                .where(member.id.eq(id))
                .execute();
    }

    @Override
    @Transactional
    public void updateRole(Long id, Role role) {
        QMember member = QMember.member;

        queryFactory.update(member)
                .set(member.role, role)
                .where(member.id.eq(id))
                .execute();
    }

    @Override
    @Transactional
    public MemberInfromationDTO getCurrentUserDetails(Long memberId) {
        QMember member = QMember.member;
        QCoachAndStudent coachAndStudent = QCoachAndStudent.coachAndStudent;

        // 멤버 기본 정보 조회
        MemberInfromationDTO memberInfromationDTO = queryFactory
                .select(Projections.fields(
                        MemberInfromationDTO.class,
                        member.id.as("memberId"),  // memberId 추가
                        member.nickName.as("nickName"),
                        member.email.as("email"),
                        member.name.as("name"),
                        member.phoneNumber.as("phoneNum"),
                        member.role.as("role")
                ))
                .from(member)
                .where(member.id.eq(memberId))
                .fetchOne();

        // 멤버가 존재하지 않을 경우 예외 발생
        if (memberInfromationDTO == null) {
            throw new BusinessException(ErrorCode.INVALID_MEMBER_ID);
        }

        // 학생일 경우, 코치 이름과 레슨 정보를 추가로 조회
        if (memberInfromationDTO.getRole() == Role.STUDENT) {
            String assignedCoach = queryFactory
                    .select(coachAndStudent.coach.member.name)
                    .from(coachAndStudent)
                    .where(coachAndStudent.student.member.id.eq(memberId))
                    .fetchOne();

            String lessonSongUrl = queryFactory
                    .select(coachAndStudent.lessonSongUrl)
                    .from(coachAndStudent)
                    .where(coachAndStudent.student.member.id.eq(memberId))
                    .fetchOne();

            memberInfromationDTO = memberInfromationDTO.withAssignedCoach(assignedCoach)
                    .withLessonSongUrl(lessonSongUrl);
        }

        return memberInfromationDTO;
    }




}
