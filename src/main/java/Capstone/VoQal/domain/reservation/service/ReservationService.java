package Capstone.VoQal.domain.reservation.service;

import Capstone.VoQal.domain.chatting.service.FCMService;
import Capstone.VoQal.domain.member.service.MemberService;
import Capstone.VoQal.domain.member.domain.Member;
import Capstone.VoQal.domain.reservation.domain.Reservation;
import Capstone.VoQal.domain.reservation.domain.Room;
import Capstone.VoQal.domain.reservation.dto.*;
import Capstone.VoQal.domain.reservation.repository.ReservationRepository;
import Capstone.VoQal.domain.reservation.repository.RoomRepository;
import Capstone.VoQal.global.enums.ErrorCode;
import Capstone.VoQal.global.error.exception.BusinessException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final MemberService memberService;
    private final EntityManager entityManager;
    private final RedissonClient redissonClient;
    private final StringRedisTemplate redisTemplate;
    private final FCMService fcmService;

    private static final String RESERVATION_LOCK_PREFIX = "reservation:lock:";
    private static final String RESERVATION_STREAM_KEY = "reservation:stream";


    @Transactional
    public AvailableTimesDTO getAvailableTimes(GetAvailableTimeDTO getAvailableTimeDTO) {
        LocalDateTime startOfDay = getAvailableTimeDTO.getRequestDate().atStartOfDay();
        LocalDateTime endOfDay = getAvailableTimeDTO.getRequestDate().atTime(LocalTime.MAX);

        List<Reservation> reservations = reservationRepository.findReservationList(getAvailableTimeDTO.getRoomId(), startOfDay, endOfDay);

        reservations.forEach(reservation -> entityManager.lock(reservation, LockModeType.PESSIMISTIC_READ));

        if (getAvailableTimeDTO.getRequestDate().isBefore(LocalDate.now())) {
            throw new BusinessException(ErrorCode.PAST_RESERVATION_NOT_ALLOWED);
        }

        List<LocalTime> allTimes = new ArrayList<>();
        for (int i = 10; i < 22; i++) {
            allTimes.add(LocalTime.of(i, 0));
        }

        List<LocalTime> reservedTimes = reservations.stream()
                .map(reservation -> reservation.getStartTime().toLocalTime().truncatedTo(ChronoUnit.HOURS))
                .toList();

        allTimes.removeAll(reservedTimes);

        List<LocalDateTime> availableDateTimes = allTimes.stream()
                .map(time -> getAvailableTimeDTO.getRequestDate().atTime(time))
                .toList();

        return AvailableTimesDTO.builder()
                .roomId(getAvailableTimeDTO.getRoomId())
                .availableTimes(availableDateTimes)
                .build();
    }

    @Retryable(value = {OptimisticLockException.class, BusinessException.class}, maxAttempts = 3)
    @Transactional
    public ReservationResponseDTO createReservation(ReservationRequestDTO reservationRequestDTO) {
        Member currentMember = memberService.getCurrentMember();

        Room room = roomRepository.findById(reservationRequestDTO.getRoomId())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REQUEST));

        LocalDateTime startTime = reservationRequestDTO.getStartTime();
        LocalDateTime endTime = reservationRequestDTO.getEndTime();

        String lockKey = RESERVATION_LOCK_PREFIX + room.getId();
        RLock lock = redissonClient.getLock(lockKey);

        try {
            boolean isLocked = lock.tryLock(5, 10, TimeUnit.SECONDS);
            if (!isLocked) {
                throw new BusinessException(ErrorCode.RESERVATION_TIME_CONFLICT);
            }

            Optional<Reservation> existingReservation = reservationRepository.findSameReservation(room.getId(), startTime, endTime);

            if (existingReservation.isPresent()) {
                throw new BusinessException(ErrorCode.RESERVATION_TIME_CONFLICT);
            }

            Reservation reservation = Reservation.builder()
                    .room(room)
                    .startTime(startTime)
                    .endTime(endTime)
                    .member(currentMember)
                    .build();

            reservationRepository.save(reservation);
            return ReservationResponseDTO.builder()
                    .roomId(reservation.getRoom().getId())
                    .startTime(reservation.getStartTime())
                    .endTime(reservation.getEndTime())
                    .status(200)
                    .build();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        } finally {
            lock.unlock();
        }
    }


    @Transactional
    public List<ReservationResponseDetailsDTO> getAllReservation() {
        Member currentMember = memberService.getCurrentMember();
        List<Reservation> reservations = reservationRepository.findNonDeletedByMemberId(currentMember.getId());
        List<ReservationResponseDetailsDTO> responseDTOS = new ArrayList<>();
        for (Reservation reservation : reservations) {
            responseDTOS.add(new ReservationResponseDetailsDTO(reservation.getRoom().getId()
                    , reservation.getId()
                    , reservation.getStartTime()
                    , reservation.getEndTime()));
        }
        return responseDTOS;
    }

    @Transactional
    public ReservationResponseDTO getReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND));

        return ReservationResponseDTO.builder()
                .roomId(reservation.getRoom().getId())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .status(200)
                .build();
    }



    @Transactional
    public void updateReservation(Long reservationId, UpdateReservationDTO updateReservationDTO) {

        Reservation existingReservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND));

        Room newRoom = roomRepository.findById(updateReservationDTO.getNewRoomId())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REQUEST));

        if (updateReservationDTO.getNewStartTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.PAST_RESERVATION_NOT_ALLOWED);
        }


        LocalDateTime newStartTime = updateReservationDTO.getNewStartTime().truncatedTo(ChronoUnit.HOURS);
        LocalDateTime newEndTime = updateReservationDTO.getNewEndTime().truncatedTo(ChronoUnit.HOURS).minusMinutes(1);

        entityManager.lock(existingReservation, LockModeType.PESSIMISTIC_WRITE);

        Optional<Reservation> conflictingReservations = reservationRepository.findSameReservation(
                newRoom.getId(), newStartTime, newEndTime);

        if (conflictingReservations.isPresent()) {
            throw new BusinessException(ErrorCode.RESERVATION_TIME_CONFLICT);
        }
        checkAvailableReservationTime(updateReservationDTO.getNewStartTime(), updateReservationDTO.getNewEndTime());


        existingReservation.changeRoom(newRoom);
        existingReservation.reschedule(newStartTime, newEndTime);

        reservationRepository.save(existingReservation);
    }


    @Transactional
    public void deleteReservation(Long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND));

        entityManager.lock(reservation, LockModeType.PESSIMISTIC_WRITE);

        reservationRepository.deleteReservation(reservation.getId());

    }

    private static void checkAvailableReservationTime(LocalDateTime startTime, LocalDateTime endTime) {
        LocalTime allowedStartTime = LocalTime.of(10, 0);
        LocalTime allowedEndTime = LocalTime.of(22, 0);

        if (startTime.toLocalTime().isBefore(allowedStartTime) || endTime.toLocalTime().isAfter(allowedEndTime)) {
            throw new BusinessException(ErrorCode.NOT_AVAILABLE_RESERVATION_TIME);
        }
    }


}
