package Capstone.VoQal.domain.challenge.repository.keyword;

import Capstone.VoQal.domain.challenge.domain.ChallengeKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordRepository extends JpaRepository<ChallengeKeyword, Long>,KeywordRepositoryCustom {

}
