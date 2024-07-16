package Capstone.VoQal.domain.reservation.repository;

import Capstone.VoQal.domain.reservation.domain.QReservation;
import Capstone.VoQal.domain.reservation.domain.Reservation;
import Capstone.VoQal.global.enums.ErrorCode;
import Capstone.VoQal.global.error.exception.BusinessException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static Capstone.VoQal.domain.reservation.domain.QReservation.reservation;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryCustomImpl implements ReservationRepositoryCustom {
    private final JPAQueryFactory queryFactory;


    @Transactional
    @Override
    public void deleteReservation(Long reservationId) {
        long updatedCount = queryFactory.update(reservation)
                .set(reservation.deletedAt, LocalDateTime.now())
                .where(reservation.id.eq(reservationId))
                .execute();

        if (updatedCount == 0) {
            throw new BusinessException(ErrorCode.RESERVATION_NOT_FOUND_OR_ALREADY_DELETED);
        }
    }

    @Transactional
    @Override
    public List<Reservation> findNonDeletedByMemberId(Long memberId) {
        LocalDateTime now = LocalDateTime.now();
        return queryFactory.selectFrom(reservation)
                .where(reservation.member.id.eq(memberId)
                        .and(reservation.deletedAt.isNull())
                        .and(reservation.startTime.after(now)))
                .fetch();
    }

    @Transactional
    @Override
    public List<Reservation> findResrvationList(Long roomId, LocalDateTime startOfDay, LocalDateTime endOfDay) {
        return queryFactory.selectFrom(reservation)
                .where(
                        reservation.room.id.eq(roomId)
                                .and(reservation.startTime.between(startOfDay, endOfDay))
                                .and(reservation.deletedAt.isNull())
                )
                .fetch();
    }

    @Transactional
    @Override
    public Optional<Reservation> findSameResrvation(Long roomId, LocalDateTime startTime, LocalDateTime endTime) {
        Reservation result = queryFactory.selectFrom(reservation)
                .where(
                        reservation.room.id.eq(roomId)
                                .and(reservation.startTime.between(startTime, endTime))
                                .and(reservation.deletedAt.isNull())
                )
                .fetchOne();
        return Optional.ofNullable(result);
    }

}
