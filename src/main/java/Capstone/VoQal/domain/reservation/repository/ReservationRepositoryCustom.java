package Capstone.VoQal.domain.reservation.repository;

import Capstone.VoQal.domain.reservation.domain.Reservation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepositoryCustom {
    void deleteReservation(Long reservationId);
    List<Reservation> findNonDeletedByMemberId(Long memberId);

    Optional<Reservation> findSameReservation(Long roomId, LocalDateTime startOfDay, LocalDateTime endOfDay);

    List<Reservation> findReservationList(Long roomId, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
