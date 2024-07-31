package Capstone.VoQal.domain.reservation.controller;

import Capstone.VoQal.domain.reservation.dto.*;
import Capstone.VoQal.domain.reservation.service.ReservationService;
import Capstone.VoQal.global.dto.MessageDTO;
import Capstone.VoQal.global.dto.ResponseWrapper;
import Capstone.VoQal.global.error.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/available-times")
    @Operation(summary = "예약 가능시간 조회 ", description = "해당 날짜와 방에 예약 가능한 시간을 리스트로 받습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "400", description = "과거의 예약은 진행할 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
    public ResponseEntity<AvailableTimesDTO> getAvailableTimes(@ModelAttribute GetAvailableTimeDTO getAvailableTimeDTO) {
        AvailableTimesDTO availableTimes = reservationService.getAvailableTimes(getAvailableTimeDTO);
        return ResponseEntity.ok(availableTimes);
    }

    @PostMapping("/reservation")
    @Operation(summary = "예약 진행", description = "필요한 정보를 입력하고 예약을 진행합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청값입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "400", description = "동일시간에 예약이 존재합니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "400", description = "과거의 예약은 진행할 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "400", description = "예약가능 시간이 아닙니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
    public ResponseEntity<ReservationResponseDTO> createReservation(@RequestBody ReservationRequestDTO reservationRequestDTO) {
        ReservationResponseDTO reservationResponseDTO = reservationService.createReservation(reservationRequestDTO);
        return ResponseEntity.ok(reservationResponseDTO);
    }

    @GetMapping("/reservation")
    @Operation(summary = "내 모든 예약 조회", description = "본인이 예약한 모든 내역들을 조회합니다.")
    public ResponseEntity<ResponseWrapper<List<ReservationResponseDetailsDTO>>> getAllReservations() {
        List<ReservationResponseDetailsDTO> reservations = reservationService.getAllReservation();
        ResponseWrapper<List<ReservationResponseDetailsDTO>> responseList = ResponseWrapper.<List<ReservationResponseDetailsDTO>>builder()
                .status(HttpStatus.OK.value())
                .data(reservations)
                .build();
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/reservation/{reservationId}")
    @Operation(summary = "예약 상세 내역 조회", description = "기존 예약에 대한 상세 내역 조회",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "400", description = "예약정보를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
    public ResponseEntity<ReservationResponseDTO> getReservation(@PathVariable("reservationId") Long reservationId) {
        ReservationResponseDTO reservation = reservationService.getReservation(reservationId);
        return ResponseEntity.ok(reservation);
    }

    @PatchMapping("/reservation/{reservationId}")
    @Operation(summary = "예약 수정", description = "기존에 존재하는 예약을 수정합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "400", description = "예약정보를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청값입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "400", description = "과거의 예약은 진행할 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "400", description = "동일시간에 예약이 존재합니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "400", description = "예약가능 시간이 아닙니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
    public ResponseEntity<MessageDTO> updateReservation(@PathVariable("reservationId") Long reservationId, @RequestBody UpdateReservationDTO updateReservationDTO) {
        reservationService.updateReservation(reservationId, updateReservationDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(MessageDTO.builder()
                        .message("성공적으로 수정되었습니다.")
                        .status(HttpStatus.OK.value())
                        .build());
    }

    @DeleteMapping("/reservation/{reservationId}")
    @Operation(summary = "예약 삭제", description = "기존에 예약했던 내역을 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "400", description = "예약정보를 찾을 수 없거나 이미 삭제된 예약입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "400", description = "예약 가능시간이 아닙니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
    public ResponseEntity<MessageDTO> deleteReservation(@PathVariable("reservationId") Long reservationId) {
        reservationService.deleteReservation(reservationId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(MessageDTO.builder()
                        .message("성공적으로 삭제되었습니다.")
                        .status(HttpStatus.OK.value())
                        .build());
    }
}
