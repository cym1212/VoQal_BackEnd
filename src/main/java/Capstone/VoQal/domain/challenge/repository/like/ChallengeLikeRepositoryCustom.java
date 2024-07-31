package Capstone.VoQal.domain.challenge.repository.like;

import Capstone.VoQal.domain.challenge.domain.ChallengeLike;
import Capstone.VoQal.domain.challenge.domain.ChallengePost;
import Capstone.VoQal.domain.challenge.dto.ChallengePostWithLikesDTO;
import Capstone.VoQal.domain.challenge.dto.GetLikedPostDTO;

import java.util.List;

public interface ChallengeLikeRepositoryCustom {

    ChallengeLike findByMemberIdAndChallengePost(Long memberId, ChallengePost challengePost);
    List<GetLikedPostDTO> findAllLikedChallengeByMemberId(Long memberId);

}
