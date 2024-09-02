package Capstone.VoQal.domain.challenge.repository.keyword;

import Capstone.VoQal.domain.challenge.domain.ChallengeKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KeywordRepository extends JpaRepository<ChallengeKeyword, Long>,KeywordRepositoryCustom {
    Optional<ChallengeKeyword> findByKeyword(String keyword);
}
