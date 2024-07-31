package Capstone.VoQal.domain.challenge.service;

import Capstone.VoQal.domain.challenge.domain.ChallengeLike;
import Capstone.VoQal.domain.challenge.domain.ChallengePost;
import Capstone.VoQal.domain.challenge.dto.ChallengePostWithLikesDTO;
import Capstone.VoQal.domain.challenge.dto.GetLikedPostDTO;
import Capstone.VoQal.domain.challenge.repository.like.ChallengeLikeRepository;
import Capstone.VoQal.domain.challenge.repository.challenge.ChallengePostRepository;
import Capstone.VoQal.domain.member.domain.Member;
import Capstone.VoQal.domain.member.service.MemberService;
import Capstone.VoQal.global.enums.ErrorCode;
import Capstone.VoQal.global.error.exception.BusinessException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ChallengeLikeService {

    private final MemberService memberService;
    private final ChallengePostRepository challengePostRepository;
    private final ChallengeLikeRepository challengeLikeRepository;

    @Transactional
    public void likeChallenge(Long challengePostId) {
        Member currentMember = memberService.getCurrentMember();
        ChallengePost challengePost = challengePostRepository.findById(challengePostId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CHALLENGE_POST_NOT_FOUND));

        ChallengeLike findchallengeLikeByMemberId = challengeLikeRepository.findByMemberIdAndChallengePost(currentMember.getId(), challengePost);
        if (findchallengeLikeByMemberId != null) {
            throw new BusinessException(ErrorCode.ALREADY_LIKED);
        }

        ChallengeLike challengeLike = ChallengeLike.builder()
                .member(currentMember)
                .challengePost(challengePost)
                .build();

        challengeLikeRepository.save(challengeLike);
    }

    @Transactional
    public void unlikeChallenge(Long challengePostId) {
        Member currentMember = memberService.getCurrentMember();
        ChallengePost challengePost = challengePostRepository.findById(challengePostId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CHALLENGE_POST_NOT_FOUND));

        ChallengeLike findchallengeLikeByMemberId = challengeLikeRepository.findByMemberIdAndChallengePost(currentMember.getId(), challengePost);
        if (findchallengeLikeByMemberId == null) {
            throw new BusinessException(ErrorCode.LIKE_NOT_FOUND);
        }
        challengeLikeRepository.delete(findchallengeLikeByMemberId);
    }

    @Transactional
    public List<GetLikedPostDTO> getLikedChallenges() {
        Member currentMember = memberService.getCurrentMember();
        return challengeLikeRepository.findAllLikedChallengeByMemberId(currentMember.getId());
    }


    @Transactional
    public List<ChallengePostWithLikesDTO> getAllChallengePostsWithLikeCount() {
        Member currentMember = memberService.getCurrentMember();
        return challengePostRepository.findAllChallengePostsWithLikeCountByMemberId(currentMember.getId());
    }
}
