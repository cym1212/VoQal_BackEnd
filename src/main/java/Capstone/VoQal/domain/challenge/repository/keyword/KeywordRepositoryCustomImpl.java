package Capstone.VoQal.domain.challenge.repository.keyword;

import Capstone.VoQal.domain.challenge.domain.ChallengeKeyword;
import Capstone.VoQal.domain.challenge.domain.QChallengeKeyword;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Repository
//@RequiredArgsConstructor
//public class KeywordRepositoryCustomImpl implements KeywordRepositoryCustom {
//
//    private final JPAQueryFactory queryFactory;
//
//    @Override
//    @Transactional
//    public ChallengeKeyword findNonUsedKeywordById(Long keywordId) {
//        QChallengeKeyword challengeKeyword = QChallengeKeyword.challengeKeyword;
//
//        ChallengeKeyword keyword = queryFactory.selectFrom(challengeKeyword)
//                .where(challengeKeyword.id.eq(keywordId)
//                        .and(challengeKeyword.used.isFalse()))
//                .fetchFirst();
//
//
//        if (keyword != null) {
//            queryFactory.update(challengeKeyword)
//                    .where(challengeKeyword.id.eq(keywordId))
//                    .set(challengeKeyword.used, true)
//                    .execute();
//        }
//
//
//        return keyword;
//    }
//
//    @Transactional
//    public void resetAllUsedKeywords() {
//        QChallengeKeyword challengeKeyword = QChallengeKeyword.challengeKeyword;
//
//        queryFactory.update(challengeKeyword)
//                .set(challengeKeyword.used, false)
//                .execute();
//    }
//
//}
@Repository
@RequiredArgsConstructor
public class KeywordRepositoryCustomImpl implements KeywordRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    @Transactional
    public List<ChallengeKeyword> findAvailableKeywords() {
        QChallengeKeyword challengeKeyword = QChallengeKeyword.challengeKeyword;

        // 사용되지 않은 모든 키워드를 한 번에 조회합니다.
        return queryFactory.selectFrom(challengeKeyword)
                .where(challengeKeyword.used.isFalse())
                .fetch();
    }



    @Transactional
    public void resetAllUsedKeywords() {
        QChallengeKeyword challengeKeyword = QChallengeKeyword.challengeKeyword;

        queryFactory.update(challengeKeyword)
                .set(challengeKeyword.used, false)
                .execute();
    }

    @Transactional
    public void markKeywordAsUsed(Long keywordId) {
        QChallengeKeyword challengeKeyword = QChallengeKeyword.challengeKeyword;

        queryFactory.update(challengeKeyword)
                .where(challengeKeyword.id.eq(keywordId))
                .set(challengeKeyword.used, true)
                .execute();
    }
}
