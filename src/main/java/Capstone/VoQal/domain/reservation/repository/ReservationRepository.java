package Capstone.VoQal.domain.reservation.repository;

import Capstone.VoQal.domain.reservation.domain.Reservation;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation,Long>, ReservationRepositoryCustom{

    Optional<Reservation> findById(Long id);

//    @Lock(LockModeType.OPTIMISTIC)
//    @Query("SELECT r FROM Reservation r WHERE r.room.id = :roomId AND r.startTime BETWEEN :startTime AND :endTime AND r.deletedAt IS NULL")
//    Optional<Reservation> findSameReservation(@Param("roomId") Long roomId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}
