package Capstone.VoQal.domain.reservation.repository;

import Capstone.VoQal.domain.reservation.domain.Reservation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation,Long>, ReservationRepositoryCustom{

    Optional<Reservation> findById(Long id);

}
