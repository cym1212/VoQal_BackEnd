package Capstone.VoQal.domain.challenge.repository.challenge;

import Capstone.VoQal.domain.challenge.domain.QChallengeLike;
import Capstone.VoQal.domain.challenge.domain.QChallengePost;
import Capstone.VoQal.domain.challenge.dto.ChallengePostWithLikesDTO;
import Capstone.VoQal.domain.challenge.dto.GetAllChallengeResponseDTO;
import Capstone.VoQal.domain.challenge.dto.GetMyChallengeResponseDTO;
import Capstone.VoQal.domain.member.domain.QMember;
import Capstone.VoQal.global.enums.ErrorCode;
import Capstone.VoQal.global.error.exception.BusinessException;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class ChallengePostRepositoryCustomImpl implements ChallengePostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    @Transactional
    public List<GetMyChallengeResponseDTO> findAllNonDeletedPostById(Long memberId) {
        QChallengePost qChallengePost = QChallengePost.challengePost;
        QMember qMember = QMember.member;
        QChallengeLike qChallengeLike = QChallengeLike.challengeLike;

        return queryFactory
                .select(Projections.constructor(
                        GetMyChallengeResponseDTO.class,
                        qChallengePost.thumbnailUrl,
                        qChallengePost.challengeRecordUrl,
                        qChallengePost.id.as("challengePostId"),
                        qChallengePost.songTitle,
                        qChallengePost.singer,
                        qMember.nickName,
                        qChallengeLike.count().as("likeCount")
                ))
                .from(qChallengePost)
                .leftJoin(qChallengePost.member, qMember)
                .leftJoin(qChallengeLike).on(qChallengeLike.challengePost.eq(qChallengePost))
                .where(qChallengePost.member.id.eq(memberId)
                        .and(qChallengePost.deletedAt.isNull()))
                .groupBy(qChallengePost.id, qMember.nickName)
                .orderBy(qChallengePost.createdAt.desc())
                .fetch();
    }

    @Override
    @Transactional
    public void updateChallengePost(Long challengePostId, String updateThumbnail, String updateRecord, String updateSongTitle, String updateSinger) {
        QChallengePost qChallengePost = QChallengePost.challengePost;

        long affectedRows = queryFactory.update(qChallengePost)
                .where(qChallengePost.id.eq(challengePostId))
                .set(qChallengePost.thumbnailUrl, updateThumbnail)
                .set(qChallengePost.challengeRecordUrl, updateRecord)
                .set(qChallengePost.songTitle, updateSongTitle)
                .set(qChallengePost.singer, updateSinger)
                .execute();

        if (affectedRows == 0) {
            throw new BusinessException(ErrorCode.REQUEST_FAILED);
        }
    }

    @Override
    @Transactional
    public void deleteChallengePost(Long challengePostId) {
        QChallengePost qChallengePost = QChallengePost.challengePost;

        LocalDateTime now = LocalDateTime.now();
        long affectedRows = queryFactory.update(qChallengePost)
                .where(qChallengePost.id.eq(challengePostId))
                .set(qChallengePost.deletedAt, now)
                .execute();

        if (affectedRows == 0) {
            throw new BusinessException(ErrorCode.REQUEST_FAILED);
        }
    }


    @Transactional
    @Override
    public Page<GetAllChallengeResponseDTO> findAllNonDeletedPostsWithLikes(Long memberId, Pageable pageable) {
        QChallengePost qChallengePost = QChallengePost.challengePost;
        QChallengeLike qChallengeLike = QChallengeLike.challengeLike;

        List<GetAllChallengeResponseDTO> posts = queryFactory.select(Projections.constructor(
                        GetAllChallengeResponseDTO.class,
                        qChallengePost.id,
                        qChallengePost.thumbnailUrl,
                        qChallengePost.challengeRecordUrl,
                        qChallengePost.songTitle,
                        qChallengePost.singer,
                        qChallengePost.member.nickName,
                        qChallengeLike.isNotNull().as("liked")
                ))
                .from(qChallengePost)
                .leftJoin(qChallengeLike).on(qChallengePost.id.eq(qChallengeLike.challengePost.id).and(qChallengeLike.member.id.eq(memberId)))
                .where(qChallengePost.deletedAt.isNull())
                .orderBy(qChallengePost.randomOrder.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.selectFrom(qChallengePost)
                .where(qChallengePost.deletedAt.isNull())
                .fetchCount();

        return new PageImpl<>(posts, pageable, total);
    }


    @Override
    @Transactional
    public void randomizePostOrder() {
        entityManager.createNativeQuery("UPDATE challenge_post SET random_order = random()")
                .executeUpdate();
    }

    @Override
    @Transactional
    public void deleteOldChallengePosts(LocalDateTime start, LocalDateTime end) {
        QChallengePost qChallengePost = QChallengePost.challengePost;

        queryFactory.update(qChallengePost)
                .where(qChallengePost.createdAt.between(start, end))
                .set(qChallengePost.deletedAt, LocalDateTime.now())
                .execute();
    }

    @Transactional
    @Override
    public List<ChallengePostWithLikesDTO> findAllChallengePostsWithLikeCountByMemberId(Long memberId) {
        QChallengePost qChallengePost = QChallengePost.challengePost;
        QChallengeLike qChallengeLike = QChallengeLike.challengeLike;

        return queryFactory.select(Projections.constructor(
                        ChallengePostWithLikesDTO.class,
                        qChallengePost.id,
                        qChallengePost.challengeRecordUrl,
                        qChallengePost.thumbnailUrl,
                        qChallengePost.songTitle,
                        qChallengePost.singer,
                        qChallengeLike.count().as("likeCount")
                ))
                .from(qChallengePost)
                .leftJoin(qChallengeLike).on(qChallengePost.id.eq(qChallengeLike.challengePost.id))
                .where(qChallengePost.member.id.eq(memberId)
                        .and(qChallengePost.deletedAt.isNull()))
                .groupBy(qChallengePost.id)
                .fetch();
    }

}
