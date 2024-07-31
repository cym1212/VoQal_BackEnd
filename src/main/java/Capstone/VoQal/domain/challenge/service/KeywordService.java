package Capstone.VoQal.domain.challenge.service;


import Capstone.VoQal.domain.challenge.domain.ChallengeKeyword;
import Capstone.VoQal.domain.challenge.repository.keyword.KeywordRepository;
import Capstone.VoQal.global.enums.ErrorCode;
import Capstone.VoQal.global.error.exception.BusinessException;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class KeywordService {

    private final KeywordRepository keywordRepository;
    private final Set<Long> usedKeywords = new HashSet<>();
    @Getter
    private String todayKeyword;
    private int used = 0;
    private static final int MAX_KEYWORDS = 100;

    // 100개의 키워드 중에서 랜덤으로 가져오되 31개 전까지는 한 번 가져왔던 키워드가 또 가져와지면 안 됨
    @Transactional
    public void updateDailyKeyword() {
        if (used >= 31) {
            usedKeywords.clear();
            keywordRepository.resetAllUsedKeywords();
            used = 0;
        }

        // 사용되지 않은 키워드를 한 번에 모두 가져옵니다.
        List<ChallengeKeyword> availableKeywords = keywordRepository.findAvailableKeywords();
        if (availableKeywords.isEmpty()) {
            throw new BusinessException(ErrorCode.KEYWORD_NOT_FOUND);
        }

        Random random = new Random();
        ChallengeKeyword keyword;
        do {
            int index = random.nextInt(availableKeywords.size());
            keyword = availableKeywords.get(index);
        } while (usedKeywords.contains(keyword.getId()));

        usedKeywords.add(keyword.getId());
        used++;
        todayKeyword = keyword.getKeyword();

        // 키워드를 사용된 상태로 업데이트합니다.
        keywordRepository.markKeywordAsUsed(keyword.getId());
    }
}
