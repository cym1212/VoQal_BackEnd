package Capstone.VoQal.global.schedule;

import Capstone.VoQal.domain.challenge.service.ChallengeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
public class ChallengePostScheduler {

    private final ChallengeService challengeService;

    @Scheduled(fixedRate = 600000) // 10분마다 실행
    @Transactional
    public void scheduleRandomizePostOrder() {
        challengeService.randomizePostOrder();
    }

    @Scheduled(cron = "0 0 4 * * ?") // 매일 새벽 4시에 실행
    @Transactional
    public void scheduleDeleteOldChallengePosts() {
        LocalDateTime start = LocalDateTime.now(ZoneId.of("Asia/Seoul")).with(LocalTime.of(4, 0, 1)).minusDays(1);
        LocalDateTime end = LocalDateTime.now(ZoneId.of("Asia/Seoul")).with(LocalTime.of(4, 0, 0));
        challengeService.deleteOldChallengePosts(start, end);
    }
}