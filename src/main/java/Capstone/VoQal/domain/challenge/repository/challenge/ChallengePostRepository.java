package Capstone.VoQal.domain.challenge.repository.challenge;

import Capstone.VoQal.domain.challenge.domain.ChallengePost;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ChallengePostRepository extends JpaRepository<ChallengePost, Long>, ChallengePostRepositoryCustom {

}
