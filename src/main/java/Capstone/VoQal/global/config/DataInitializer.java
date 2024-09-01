package Capstone.VoQal.global.config;

import Capstone.VoQal.domain.challenge.domain.ChallengeKeyword;
import Capstone.VoQal.domain.challenge.repository.keyword.KeywordRepository;
import Capstone.VoQal.domain.reservation.domain.Room;
import Capstone.VoQal.domain.reservation.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final RoomRepository roomRepository;
    private final KeywordRepository keywordRepository;

    @Bean
    @Transactional
    public CommandLineRunner initializeRooms() {
        return args -> {
            List<String> roomNames = List.of("1번방", "2번방", "3번방", "4번방", "5번방");

            List<String> existingRoomNames = roomRepository.findAllByRoomNameIn(roomNames)
                    .stream()
                    .map(Room::getRoomName)
                    .collect(Collectors.toList());

            List<String> newRoomNames = roomNames.stream()
                    .filter(roomName -> !existingRoomNames.contains(roomName))
                    .collect(Collectors.toList());

            newRoomNames.forEach(roomName -> roomRepository.save(new Room(roomName)));
        };
    }

    @Bean
    @Transactional
    public CommandLineRunner initializeKeywords() {
        return args -> {
            long count = keywordRepository.count();
            if (count == 0) {
                List<ChallengeKeyword> keywords = Arrays.asList(
                        new ChallengeKeyword("기쁨", false, null),
                        new ChallengeKeyword("슬픔", false, null),
                        new ChallengeKeyword("화남", false, null),
                        new ChallengeKeyword("행복", false, null),
                        new ChallengeKeyword("우울", false, null),
                        new ChallengeKeyword("평온", false, null),
                        new ChallengeKeyword("감사", false, null),
                        new ChallengeKeyword("사랑", false, null),
                        new ChallengeKeyword("희망", false, null),
                        new ChallengeKeyword("미안함", false, null),
                        new ChallengeKeyword("외로움", false, null),
                        new ChallengeKeyword("분노", false, null),
                        new ChallengeKeyword("피곤", false, null),
                        new ChallengeKeyword("여유", false, null),
                        new ChallengeKeyword("열정", false, null),
                        new ChallengeKeyword("위로", false, null),
                        new ChallengeKeyword("슬픔", false, null),
                        new ChallengeKeyword("후회", false, null),
                        new ChallengeKeyword("설렘", false, null),
                        new ChallengeKeyword("불안", false, null),
                        new ChallengeKeyword("고통", false, null),
                        new ChallengeKeyword("즐거움", false, null),
                        new ChallengeKeyword("스트레스", false, null),
                        new ChallengeKeyword("용기", false, null),
                        new ChallengeKeyword("감동", false, null),
                        new ChallengeKeyword("환희", false, null),
                        new ChallengeKeyword("짜릿함", false, null),
                        new ChallengeKeyword("봄", false, null),
                        new ChallengeKeyword("여름", false, null),
                        new ChallengeKeyword("가을", false, null),
                        new ChallengeKeyword("겨울", false, null),
                        new ChallengeKeyword("술", false, null),
                        new ChallengeKeyword("별", false, null),
                        new ChallengeKeyword("몽환적인", false, null),
                        new ChallengeKeyword("발라드", false, null),
                        new ChallengeKeyword("락", false, null),
                        new ChallengeKeyword("ost", false, null),
                        new ChallengeKeyword("시티팝", false, null),
                        new ChallengeKeyword("뮤지컬", false, null),
                        new ChallengeKeyword("밴드", false, null),
                        new ChallengeKeyword("인디", false, null),
                        new ChallengeKeyword("팝", false, null),
                        new ChallengeKeyword("비", false, null)
                );
                keywordRepository.saveAll(keywords);
            }
        };
    }
}
