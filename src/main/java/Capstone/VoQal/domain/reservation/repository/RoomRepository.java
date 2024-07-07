package Capstone.VoQal.domain.reservation.repository;

import Capstone.VoQal.domain.reservation.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room,Long> {
    List<Room> findAllByRoomNameIn(List<String> roomNames);
}
