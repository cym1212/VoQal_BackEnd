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

import java.util.List;
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

        String role = String.valueOf(queryFactory.select(member.role)
                .from(member)
                .where(member.id.eq(memberId))
                .fetchOne());

        return role;
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

        MemberInfromationDTO memberInfromationDTO = queryFactory
                .select(Projections.constructor(
                        MemberInfromationDTO.class,
                        member.nickName,
                        member.email,
                        member.name,
                        member.phoneNumber,
                        member.role,
                        coachAndStudent.lessonSongUrl
                ))
                .from(member)
                .leftJoin(coachAndStudent)
                .on(coachAndStudent.student.member.id.eq(member.id))
                .where(member.id.eq(memberId))
                .fetchOne();

        if (memberInfromationDTO == null) {
            throw new BusinessException(ErrorCode.INVALID_MEMBER_ID);
        }

        return memberInfromationDTO;
    }

}
