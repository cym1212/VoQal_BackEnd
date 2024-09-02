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
                        new ChallengeKeyword("기쁨", false, null, "FFFF66"),
                        new ChallengeKeyword("슬픔", false, null, "708090"),
                        new ChallengeKeyword("화남", false, null, "DC143C"),
                        new ChallengeKeyword("행복", false, null, "FFD700"),
                        new ChallengeKeyword("우울", false, null, "2F4F4F"),
                        new ChallengeKeyword("평온", false, null, "ADD8E6"),
                        new ChallengeKeyword("감사", false, null, "FFD700"),
                        new ChallengeKeyword("사랑", false, null, "FF1493"),
                        new ChallengeKeyword("희망", false, null, "00FA9A"),
                        new ChallengeKeyword("미안함", false, null, "B0C4DE"),
                        new ChallengeKeyword("외로움", false, null, "696969"),
                        new ChallengeKeyword("분노", false, null, "DC143C"),
                        new ChallengeKeyword("피곤", false, null, "A9A9A9"),
                        new ChallengeKeyword("여유", false, null, "98FB98"),
                        new ChallengeKeyword("열정", false, null, "FF4500"),
                        new ChallengeKeyword("위로", false, null, "87CEEB"),
                        new ChallengeKeyword("슬픔", false, null, "4682B4"),
                        new ChallengeKeyword("후회", false, null, "708090"),
                        new ChallengeKeyword("설렘", false, null, "FFE4E1"),
                        new ChallengeKeyword("불안", false, null, "A52A2A"),
                        new ChallengeKeyword("고통", false, null, "8B0000"),
                        new ChallengeKeyword("즐거움", false, null, "FFFF00"),
                        new ChallengeKeyword("스트레스", false, null, "B22222"),
                        new ChallengeKeyword("용기", false, null, "FFA500"),
                        new ChallengeKeyword("감동", false, null, "6A5ACD"),
                        new ChallengeKeyword("환희", false, null, "FFD700"),
                        new ChallengeKeyword("짜릿함", false, null, "FF4500"),
                        new ChallengeKeyword("봄", false, null, "FF8484"),
                        new ChallengeKeyword("여름", false, null, "6D81FF"),
                        new ChallengeKeyword("가을", false, null, "CD853F"),
                        new ChallengeKeyword("겨울", false, null, "A4D4FF"),
                        new ChallengeKeyword("술", false, null, "3CB371"),
                        new ChallengeKeyword("별", false, null, "FFFACD"),
                        new ChallengeKeyword("몽환적인", false, null, "9370DB"),
                        new ChallengeKeyword("발라드", false, null, "B0C4DE"),
                        new ChallengeKeyword("락", false, null, "8B0000"),
                        new ChallengeKeyword("ost", false, null, "4682B4"),
                        new ChallengeKeyword("시티팝", false, null, "B19CD9"),
                        new ChallengeKeyword("뮤지컬", false, null, "FF6347"),
                        new ChallengeKeyword("밴드", false, null, "FFD700"),
                        new ChallengeKeyword("인디", false, null, "7F8C8D"),
                        new ChallengeKeyword("팝", false, null, "FF69B4"),
                        new ChallengeKeyword("비", false, null, "4682B4")
                );
                keywordRepository.saveAll(keywords);
            }
        };
    }
}
