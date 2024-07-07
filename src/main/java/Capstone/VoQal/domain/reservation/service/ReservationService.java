package Capstone.VoQal.domain.reservation.service;

import Capstone.VoQal.domain.member.service.MemberService;
import Capstone.VoQal.domain.member.domain.Member;
import Capstone.VoQal.domain.reservation.domain.Reservation;
import Capstone.VoQal.domain.reservation.domain.Room;
import Capstone.VoQal.domain.reservation.dto.*;
import Capstone.VoQal.domain.reservation.repository.ReservationRepository;
import Capstone.VoQal.domain.reservation.repository.RoomRepository;
import Capstone.VoQal.global.enums.ErrorCode;
import Capstone.VoQal.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final MemberService memberService;

//    @Transactional(readOnly = true)
//    public AvailableTimesDTO getAvailableTimes(GetAvailableTimeDTO getAvailableTimeDTO) {
//        LocalDateTime startOfDay = getAvailableTimeDTO.getRequestDate().atStartOfDay();
//        LocalDateTime endOfDay = getAvailableTimeDTO.getRequestDate().atTime(LocalTime.MAX);
//
//        List<Reservation> reservations = reservationRepository.findSameResrvationList(getAvailableTimeDTO.getRoomId(), startOfDay, endOfDay);
//
//        if (getAvailableTimeDTO.getRequestDate().isBefore(LocalDate.now())) {
//            throw new BusinessException(ErrorCode.PAST_RESERVATION_NOT_ALLOWED);
//        }
//
//        List<LocalDateTime> allTimes = new ArrayList<>();
//        for (int i = 0; i < 24; i++) {
//            allTimes.add(startOfDay.plusHours(i));
//        }
//
//        List<LocalDateTime> reservedTimes = reservations.stream()
//                .map(Reservation::getStartTime)
//                .toList();
//
//        allTimes.removeAll(reservedTimes);
//
//        return AvailableTimesDTO.builder()
//                .roomId(getAvailableTimeDTO.getRoomId())
//                .availableTimes(allTimes)
//                .build();
//    }



    // todo 예약시간 단위를 시간까지로 제한하는 설정 필요
    @Transactional(readOnly = true)
    public AvailableTimesDTO getAvailableTimes(GetAvailableTimeDTO getAvailableTimeDTO) {
        LocalDateTime startOfDay = getAvailableTimeDTO.getRequestDate().atStartOfDay();
        LocalDateTime endOfDay = getAvailableTimeDTO.getRequestDate().atTime(LocalTime.MAX);

        List<Reservation> reservations = reservationRepository.findSameResrvationList(getAvailableTimeDTO.getRoomId(), startOfDay, endOfDay);

        if (getAvailableTimeDTO.getRequestDate().isBefore(LocalDate.now())) {
            throw new BusinessException(ErrorCode.PAST_RESERVATION_NOT_ALLOWED);
        }

        List<LocalTime> allTimes = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
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


    @Transactional
    public ReservationResponseDTO createReservation(ReservationRequestDTO reservationRequestDTO) {
        Member currentMember = memberService.getCurrentMember();

        Room room = roomRepository.findById(reservationRequestDTO.getRoomId())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REQUEST));

        Optional<Reservation> checkReservation = reservationRepository.findSameResrvation(
                room.getId(),
                reservationRequestDTO.getStartTime(),
                reservationRequestDTO.getEndTime()
        );

        if (checkReservation.isPresent()) {
            throw new BusinessException(ErrorCode.RESERVATION_TIME_CONFLICT);
        }

        Reservation reservation = Reservation.builder()
                .room(room)
                .startTime(reservationRequestDTO.getStartTime())
                .endTime(reservationRequestDTO.getEndTime())
                .member(currentMember)
                .build();

        reservationRepository.save(reservation);

        return ReservationResponseDTO.builder()
                .roomId(reservation.getRoom().getId())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .build();
    }

    @Transactional
    public List<ReservationResponseDetailsDTO> getAllReservation() {
        Member currentMember = memberService.getCurrentMember();
        List<Reservation> reservations = reservationRepository.findNonDeletedByMemberId(currentMember.getId());
        List<ReservationResponseDetailsDTO> responseDTOS = new ArrayList<>();
        for (Reservation reservation : reservations) {
            responseDTOS.add(new ReservationResponseDetailsDTO(reservation.getRoom().getId()
                    ,reservation.getId()
                    ,reservation.getStartTime()
                    ,reservation.getEndTime()));
        }
        return responseDTOS;
    }

    @Transactional
    public ReservationResponseDTO getReservation(Long reservationId) {
        Optional<Reservation> reservationDetails = reservationRepository.findById(reservationId);

        Reservation reservation = reservationDetails.orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND));

        return ReservationResponseDTO.builder()
                .roomId(reservation.getRoom().getId())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .build();
    }
    

    @Transactional
    public void updateReservation(Long reservationId, UpdateReservationDTO updateReservationDTO) {

        Reservation existingReservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND));

        Room newRoom = roomRepository.findById(updateReservationDTO.getNewRoomId())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REQUEST));

        LocalDateTime newStartTime = updateReservationDTO.getNewStartTime();
        LocalDateTime newEndTime = updateReservationDTO.getNewEndTime();

        Optional<Reservation> conflictingReservations = reservationRepository.findSameResrvation(
                newRoom.getId(), newStartTime, newEndTime);

        if (conflictingReservations.isPresent() && !conflictingReservations.get().getId().equals(reservationId)) {
            throw new BusinessException(ErrorCode.RESERVATION_TIME_CONFLICT);
        }

        existingReservation.changeRoom(newRoom);
        existingReservation.reschedule(newStartTime, newEndTime);

        reservationRepository.save(existingReservation);
    }



    @Transactional
    public void deleteReservation(Long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND));
        reservationRepository.deleteReservation(reservation.getId());

    }


}
