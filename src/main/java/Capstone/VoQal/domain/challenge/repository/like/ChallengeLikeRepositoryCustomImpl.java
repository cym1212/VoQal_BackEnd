package Capstone.VoQal.domain.challenge.repository.like;

import Capstone.VoQal.domain.challenge.domain.ChallengeLike;
import Capstone.VoQal.domain.challenge.domain.ChallengePost;
import Capstone.VoQal.domain.challenge.domain.QChallengeLike;
import Capstone.VoQal.domain.challenge.domain.QChallengePost;
import Capstone.VoQal.domain.challenge.dto.GetLikedPostDTO;
import Capstone.VoQal.domain.member.domain.QMember;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChallengeLikeRepositoryCustomImpl implements ChallengeLikeRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Transactional
    @Override
    public ChallengeLike findByMemberIdAndChallengePost(Long memberId, ChallengePost challengePost) {
        QChallengeLike qChallengeLike = QChallengeLike.challengeLike;
        QChallengePost qChallengePost = QChallengePost.challengePost;
        QMember qMember = QMember.member;

        return queryFactory.selectFrom(qChallengeLike)
                .leftJoin(qChallengeLike.challengePost, qChallengePost)
                .leftJoin(qChallengeLike.member, qMember)
                .where(qChallengeLike.member.id.eq(memberId)
                        .and(qChallengeLike.challengePost.eq(challengePost))
                        .and(qChallengePost.deletedAt.isNull()))
                .fetchFirst();
    }

    @Transactional
    @Override
    public List<GetLikedPostDTO> findAllLikedChallengeByMemberId(Long memberId) {
        QChallengeLike qChallengeLike = QChallengeLike.challengeLike;
        QChallengePost qChallengePost = QChallengePost.challengePost;
        QMember qMember = QMember.member;

        return queryFactory.select(Projections.constructor(
                        GetLikedPostDTO.class,
                        qChallengePost.id,
                        qChallengePost.challengeRecordUrl,
                        qChallengePost.thumbnailUrl,
                        qChallengePost.songTitle,
                        qChallengePost.singer))
                .from(qChallengeLike)
                .leftJoin(qChallengeLike.challengePost, qChallengePost)
                .leftJoin(qChallengeLike.member, qMember)
                .where(qChallengeLike.member.id.eq(memberId)
                        .and(qChallengePost.deletedAt.isNull()))
                .fetch();
    }


}
