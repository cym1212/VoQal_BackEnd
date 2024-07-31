package Capstone.VoQal.domain.challenge.repository.like;

import Capstone.VoQal.domain.challenge.domain.ChallengeLike;
import Capstone.VoQal.domain.challenge.domain.ChallengePost;
import Capstone.VoQal.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChallengeLikeRepository extends JpaRepository<ChallengeLike, Long> , ChallengeLikeRepositoryCustom{

    Optional<ChallengeLike> findByMemberAndChallengePost(Member member, ChallengePost challengePost);
    List<ChallengePost> findAllByMemberId(Long memberId);
    long countByChallengePost(ChallengePost challengePost);
}