package Capstone.VoQal.domain.challenge.repository.challenge;

import Capstone.VoQal.domain.challenge.domain.ChallengePost;
import Capstone.VoQal.domain.challenge.domain.QChallengePost;
import Capstone.VoQal.domain.member.domain.QMember;
import Capstone.VoQal.global.enums.ErrorCode;
import Capstone.VoQal.global.error.exception.BusinessException;
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
public class ChallengeRepositoryCustomImpl implements ChallengeRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public List<ChallengePost> findAllNonDeletedPostById(Long memberId) {
        QChallengePost qChallengePost = QChallengePost.challengePost;
        QMember qMember = QMember.member;

        return queryFactory.selectFrom(qChallengePost)
                .leftJoin(qChallengePost.member, qMember).fetchJoin()
                .where(qChallengePost.member.id.eq(memberId)
                        .and(qChallengePost.deletedAt.isNull()))
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

    @Override
    public Page<ChallengePost> findAllNonDeletedPosts(Pageable pageable) {
        QChallengePost qChallengePost = QChallengePost.challengePost;

        List<ChallengePost> posts = queryFactory.selectFrom(qChallengePost)
                .where(qChallengePost.deletedAt.isNull())
                .orderBy(qChallengePost.randomOrder.asc()) // random_order 필드를 사용하여 랜덤 정렬
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

}
