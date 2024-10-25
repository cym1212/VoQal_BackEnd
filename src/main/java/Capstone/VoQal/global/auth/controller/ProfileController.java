package Capstone.VoQal.global.auth.controller;


import Capstone.VoQal.global.auth.dto.*;
import Capstone.VoQal.global.auth.service.ProfileService;
import Capstone.VoQal.global.dto.MessageDTO;
import Capstone.VoQal.global.dto.ResponseWrapper;
import Capstone.VoQal.global.error.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/role/coach")
    @Operation(summary = " 코치로 역할 설정 로직 ", description = "역할을 코치로 설정할 경우",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 멤버ID입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "400", description = "토큰 관련 오류.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })

    public ResponseEntity<GeneratedTokenDTO> setRoleCoach() {
        GeneratedTokenDTO generatedTokenDTO = profileService.setRoleToCoach();

        return ResponseEntity.status(HttpStatus.OK).body(generatedTokenDTO);
    }

    @GetMapping("/role/coach")
    @Operation(summary = " 코치 조회 ", description = "학생일 경우를 선택했을 때 코치 리스트 조회")
    public ResponseEntity<ResponseWrapper<List<MemberListDTO>>> getCoachList() {
        List<MemberListDTO> coachList = profileService.getCoachList();
        ResponseWrapper<List<MemberListDTO>> memberList = ResponseWrapper.<List<MemberListDTO>>builder()
                .status(HttpStatus.OK.value())
                .data(coachList)
                .build();
        return ResponseEntity.ok(memberList);
    }

    @PatchMapping("/{id}/change-nickname")
    @Operation(summary = " 닉네임 변경 ", description = "닉네임을 변경하는 로직",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "400", description = "이미 사용중인 닉네임입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
    public ResponseEntity<MessageDTO> updateNickname(@PathVariable("id") Long id, @RequestBody ChangeNicknameDTO changeNicknameDTO) {
        profileService.updateNickname(id, changeNicknameDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(MessageDTO.builder()
                        .message("닉네임 변경에 성공했습니다.")
                        .status(HttpStatus.OK.value())
                        .build());

    }

    @PostMapping("/request")
    @Operation(summary = " 담당 코치 신청 ", description = "코치에게 담당코치로 신청합니다")
    public ResponseEntity<MessageDTO> requestCoach(@RequestBody StudentRequestDTO studentRequestDTO) {
        Long coachId = studentRequestDTO.getCoachId();
        profileService.requestCoach(coachId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(MessageDTO.builder()
                        .message("코치 신청이 완료되었습니다.")
                        .status(HttpStatus.OK.value())
                        .build());
    }

    @GetMapping("/request")
    @Operation(summary = " 코치에게 신청한 학생 조회 ", description = "코치에게 담당코치로 신청한 학생의 목록을 조회합니다")
    public ResponseEntity<ResponseWrapper<List<RequestStudentListDTO>>> getRequestStudentList() {
        List<RequestStudentListDTO> requestStudentList = profileService.getRequestStudentList();
        ResponseWrapper<List<RequestStudentListDTO>> requestDTO = ResponseWrapper.<List<RequestStudentListDTO>>builder()
                .status(HttpStatus.OK.value())
                .data(requestStudentList)
                .build();
        return ResponseEntity.ok(requestDTO);
    }

    @PostMapping("/approve")
    @Operation(summary = " 코치에게 신청한 학생 승인 ", description = "코치에게 담당코치로 신청한 학생을 승인하여 담당 코치와 학생 관계가 됩니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "400", description = "신청 상태가 아닙니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청값입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    public ResponseEntity<MessageDTO> approveRequest(@RequestBody ApproveRequestDTO approveRequestDTO) {
        Long studentId = approveRequestDTO.getStudentId();
        profileService.approveRequest(studentId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(MessageDTO.builder()
                        .message("승인됐습니다.")
                        .status(HttpStatus.OK.value())
                        .build());
    }

    @PostMapping("/reject")
    @Operation(summary = " 코치에게 신청한 학생 거절 ", description = "코치에게 담당코치로 신청한 학생을 거절합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "400", description = "신청 상태가 아닙니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청값입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    public ResponseEntity<MessageDTO> rejectRequest(@RequestBody ApproveRequestDTO approveRequestDTO) {
        Long studentId = approveRequestDTO.getStudentId();
        profileService.rejectRequest(studentId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(MessageDTO.builder()
                        .message("거절했습니다")
                        .status(HttpStatus.OK.value())
                        .build());
    }

    @GetMapping("/student")
    @Operation(summary = "담당 학생 조회 ", description = "본인이 담당하는 학생들의 리스트를 조회합니다.")
    public ResponseEntity<ResponseWrapper<List<StudentListDTO>>> studentList() {
        List<StudentListDTO> studentList = profileService.getStudentList();
        ResponseWrapper<List<StudentListDTO>> memberList = ResponseWrapper.<List<StudentListDTO>>builder()
                .status(HttpStatus.OK.value())
                .data(studentList)
                .build();
        return ResponseEntity.ok(memberList);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "담당 학생 삭제 ", description = "본인이 담당하는 학생을 삭제합니다.")
    public ResponseEntity<MessageDTO> deleteStudent(@PathVariable("id") Long id) throws ExecutionException, InterruptedException {
        MessageDTO deleted = profileService.deleteStudent(id);
        return ResponseEntity.ok(deleted);
    }

    @GetMapping("/user/details")
    @Operation(summary = "회원 정보 확인", description = "현재 로그인한 회원의 정보를 확인합니다.")
    public ResponseEntity<ResponseWrapper<MemberInfromationDTO>> getCurrentUserDetails() {
        MemberInfromationDTO infromationDTO = profileService.getCurrentUserDetails();
        ResponseWrapper<MemberInfromationDTO> resultData = ResponseWrapper.<MemberInfromationDTO>builder()
                .status(HttpStatus.OK.value())
                .data(infromationDTO)
                .build();
        return ResponseEntity.ok().body(resultData);
    }

    @GetMapping("/status/check")
    @Operation(summary = "신청 정보 확인", description = "현재 학생의 신청상태를 확인합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "400", description = "역할이 학생이 아닙니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
    public ResponseEntity<ResponseWrapper<StudentStatusDTO>> getStudentStatus() {
        StudentStatusDTO studentStatusDTO = profileService.getStudentStatus();
        ResponseWrapper<StudentStatusDTO> resultStatus = ResponseWrapper.<StudentStatusDTO>builder()
                .status(HttpStatus.OK.value())
                .data(studentStatusDTO)
                .build();
        return ResponseEntity.ok().body(resultStatus);
    }

}
