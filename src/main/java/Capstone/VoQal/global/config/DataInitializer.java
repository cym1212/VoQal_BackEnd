package Capstone.VoQal.global.config;

import Capstone.VoQal.domain.reservation.domain.Room;
import Capstone.VoQal.domain.reservation.repository.RoomRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initializeRooms(RoomRepository roomRepository) {
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
}
