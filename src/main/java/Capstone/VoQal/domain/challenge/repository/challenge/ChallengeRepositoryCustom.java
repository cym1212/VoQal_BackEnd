package Capstone.VoQal.domain.challenge.repository.challenge;

import Capstone.VoQal.domain.challenge.domain.ChallengePost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ChallengeRepositoryCustom {

    List<ChallengePost> findAllNonDeletedPostById(Long memberId);

    void updateChallengePost(Long challengePostId, String updateThumbnail, String updateRecord, String updateSongTitle, String updateSinger);

    void deleteChallengePost(Long challengePostId);

    Page<ChallengePost> findAllNonDeletedPosts(Pageable pageable);

    void randomizePostOrder();

    void deleteOldChallengePosts(LocalDateTime start, LocalDateTime end);
}
