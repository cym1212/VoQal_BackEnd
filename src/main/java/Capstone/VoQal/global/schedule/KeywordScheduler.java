package Capstone.VoQal.global.schedule;


import Capstone.VoQal.domain.challenge.service.KeywordService;
import Capstone.VoQal.global.error.exception.BusinessException;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//@Component
//@RequiredArgsConstructor
//public class KeywordScheduler {
//
//    private final KeywordService keywordService;
//
//    @PostConstruct
//    public void init() {
//        scheduleDailyKeyword();
//    }
//    @Scheduled(cron = "0 4 * * * ?")
//    @Transactional
//    public void scheduleDailyKeyword() {
//        keywordService.updateDailyKeyword();
//    }
//}
@Component
@RequiredArgsConstructor
public class KeywordScheduler {

    private final KeywordService keywordService;

    @PostConstruct
    public void init() {
        try {
            keywordService.updateDailyKeyword();
        } catch (BusinessException e) {
            System.err.println("Initial keyword setup failed: " + e.getMessage());
        }
    }

    @Scheduled(cron = "0 4 * * * ?")
    @Transactional
    public void scheduleDailyKeyword() {
        keywordService.updateDailyKeyword();
    }
}
