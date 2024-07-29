package Capstone.VoQal.domain.challenge.repository.keyword;

import Capstone.VoQal.domain.challenge.domain.ChallengeKeyword;

import java.util.List;

public interface KeywordRepositoryCustom {

//    ChallengeKeyword findNonUsedKeywordById(Long keywordId);

    void resetAllUsedKeywords();

    List<ChallengeKeyword> findAvailableKeywords();

    void markKeywordAsUsed(Long keywordId);
}
