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
                        new ChallengeKeyword("두려움", false, null),
                        new ChallengeKeyword("역겨움", false, null),
                        new ChallengeKeyword("놀람", false, null),
                        new ChallengeKeyword("행복", false, null),
                        new ChallengeKeyword("우울", false, null),
                        new ChallengeKeyword("만족", false, null),
                        new ChallengeKeyword("흥분", false, null),
                        new ChallengeKeyword("지루함", false, null),
                        new ChallengeKeyword("평온", false, null),
                        new ChallengeKeyword("불안", false, null),
                        new ChallengeKeyword("당황", false, null),
                        new ChallengeKeyword("질투", false, null),
                        new ChallengeKeyword("감사", false, null),
                        new ChallengeKeyword("사랑", false, null),
                        new ChallengeKeyword("죄책감", false, null),
                        new ChallengeKeyword("희망", false, null),
                        new ChallengeKeyword("절망", false, null),
                        new ChallengeKeyword("좌절", false, null),
                        new ChallengeKeyword("안도", false, null),
                        new ChallengeKeyword("실망", false, null),
                        new ChallengeKeyword("신뢰", false, null),
                        new ChallengeKeyword("배신", false, null),
                        new ChallengeKeyword("부끄러움", false, null),
                        new ChallengeKeyword("미안함", false, null),
                        new ChallengeKeyword("외로움", false, null),
                        new ChallengeKeyword("쓸쓸함", false, null),
                        new ChallengeKeyword("짜증", false, null),
                        new ChallengeKeyword("분노", false, null),
                        new ChallengeKeyword("혼란", false, null),
                        new ChallengeKeyword("기운", false, null),
                        new ChallengeKeyword("힘듦", false, null),
                        new ChallengeKeyword("피곤", false, null),
                        new ChallengeKeyword("여유", false, null),
                        new ChallengeKeyword("성취감", false, null),
                        new ChallengeKeyword("만족감", false, null),
                        new ChallengeKeyword("열정", false, null),
                        new ChallengeKeyword("무관심", false, null),
                        new ChallengeKeyword("위로", false, null),
                        new ChallengeKeyword("동정", false, null),
                        new ChallengeKeyword("슬픔", false, null),
                        new ChallengeKeyword("후회", false, null),
                        new ChallengeKeyword("두근거림", false, null),
                        new ChallengeKeyword("긴장", false, null),
                        new ChallengeKeyword("설렘", false, null),
                        new ChallengeKeyword("흥분", false, null),
                        new ChallengeKeyword("안정감", false, null),
                        new ChallengeKeyword("불안감", false, null),
                        new ChallengeKeyword("두려움", false, null),
                        new ChallengeKeyword("기대감", false, null),
                        new ChallengeKeyword("자신감", false, null),
                        new ChallengeKeyword("열등감", false, null),
                        new ChallengeKeyword("자부심", false, null),
                        new ChallengeKeyword("긍지", false, null),
                        new ChallengeKeyword("자랑스러움", false, null),
                        new ChallengeKeyword("황홀함", false, null),
                        new ChallengeKeyword("충족", false, null),
                        new ChallengeKeyword("행복감", false, null),
                        new ChallengeKeyword("흡족함", false, null),
                        new ChallengeKeyword("만족함", false, null),
                        new ChallengeKeyword("뿌듯함", false, null),
                        new ChallengeKeyword("자책", false, null),
                        new ChallengeKeyword("후회감", false, null),
                        new ChallengeKeyword("불쾌감", false, null),
                        new ChallengeKeyword("황당", false, null),
                        new ChallengeKeyword("고통", false, null),
                        new ChallengeKeyword("즐거움", false, null),
                        new ChallengeKeyword("스트레스", false, null),
                        new ChallengeKeyword("행복", false, null),
                        new ChallengeKeyword("위안", false, null),
                        new ChallengeKeyword("용기", false, null),
                        new ChallengeKeyword("놀라움", false, null),
                        new ChallengeKeyword("기쁨", false, null),
                        new ChallengeKeyword("황홀", false, null),
                        new ChallengeKeyword("안심", false, null),
                        new ChallengeKeyword("신뢰", false, null),
                        new ChallengeKeyword("불안", false, null),
                        new ChallengeKeyword("두려움", false, null),
                        new ChallengeKeyword("감동", false, null),
                        new ChallengeKeyword("분노", false, null),
                        new ChallengeKeyword("애정", false, null),
                        new ChallengeKeyword("환희", false, null),
                        new ChallengeKeyword("고마움", false, null),
                        new ChallengeKeyword("사랑", false, null),
                        new ChallengeKeyword("질투", false, null),
                        new ChallengeKeyword("미움", false, null),
                        new ChallengeKeyword("경멸", false, null),
                        new ChallengeKeyword("애도", false, null),
                        new ChallengeKeyword("성취", false, null),
                        new ChallengeKeyword("기대", false, null),
                        new ChallengeKeyword("짜릿함", false, null),
                        new ChallengeKeyword("열정", false, null),
                        new ChallengeKeyword("희열", false, null),
                        new ChallengeKeyword("가벼움", false, null),
                        new ChallengeKeyword("무거움", false, null),
                        new ChallengeKeyword("기운", false, null),
                        new ChallengeKeyword("어지러움", false, null),
                        new ChallengeKeyword("만족", false, null)
                );
                keywordRepository.saveAll(keywords);
            }
        };
    }
}
