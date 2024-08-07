package Capstone.VoQal.domain.lesson.record.controller;

import Capstone.VoQal.domain.lesson.record.dto.*;
import Capstone.VoQal.domain.lesson.record.service.RecordService;
import Capstone.VoQal.global.dto.MessageDTO;
import Capstone.VoQal.global.dto.ResponseWrapper;
import Capstone.VoQal.global.error.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;


    @PostMapping(value = "/create/record",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "녹음파일 업로드 ", description = "담당학생의 녹음파일을 업로드합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "400", description = "코치가 아닙니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
    public ResponseEntity<ResponseWrapper<UploadResponseDTO>> uploadRecord( @RequestPart(value = "recordFile")MultipartFile recordFile,@RequestPart(value = "request",required = false) UploadRequestDTO request) {
        UploadResponseDTO uploadResponseDTO = recordService.uploadRecord(recordFile, request);
        ResponseWrapper<UploadResponseDTO> responseWrapper = ResponseWrapper.<UploadResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .data(uploadResponseDTO)
                .build();
        return ResponseEntity.ok(responseWrapper);
    }

    @GetMapping("/record")
    @Operation(summary = "녹음파일 전체 조회 ", description = "담당학생의 녹음파일을 전체 조회합니다.")
    public ResponseEntity<ResponseWrapper<List<LessonRecordResponseDTO>>> getAllRecordsByCoach(@ModelAttribute GetRecordUrlDTO getRecordUrlDTO) {
        List<LessonRecordResponseDTO> responseDTOS = recordService.getAllRecordsByCoach(getRecordUrlDTO);
        ResponseWrapper<List<LessonRecordResponseDTO>> result = ResponseWrapper.<List<LessonRecordResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .data(responseDTOS)
                .build();

        return ResponseEntity.ok(result);
    }



    @PatchMapping(value = "/record/{recordId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "녹음파일 수정", description = "담당학생의 녹음파일을 수정합니다.")
    public ResponseEntity<ResponseWrapper<UploadResponseDTO>> updateRecord(@PathVariable("recordId") Long recordId,
                                                                           @RequestPart("recordFile") MultipartFile recordFile,
                                                                           @RequestPart(value = "request", required = false) UpdateUploadRequestDTO request) {
        UploadResponseDTO uploadResponseDTO = recordService.updateRecord(recordId, recordFile, request);
        ResponseWrapper<UploadResponseDTO> responseWrapper = ResponseWrapper.<UploadResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .data(uploadResponseDTO)
                .build();
        return ResponseEntity.ok(responseWrapper);
    }


    @DeleteMapping("/record/{recordId}")
    @Operation(summary = "녹음파일 삭제 ", description = "담당학생의 녹음파일을 삭제합니다.")
    public ResponseEntity<MessageDTO> deleteRecord (@PathVariable("recordId") Long recordId) {
        recordService.deleteRecord(recordId);
        return ResponseEntity.ok().body(MessageDTO.builder()
                .status(HttpStatus.OK.value())
                .message("성공적으로 삭제되었습니다")
                .build());
    }

    @GetMapping("/record/student")
    @Operation(summary = "녹음파일 전체 조회 - 학생입장 ", description = "담당코치가 작성한 녹음파일 전체 조회합니다.")
    public ResponseEntity<ResponseWrapper<List<LessonRecordResponseDTO>>> getAllRecordsForStudent() {
        List<LessonRecordResponseDTO> responseDTOS = recordService.getAllRecordsForStudent();
        ResponseWrapper<List<LessonRecordResponseDTO>> result = ResponseWrapper.<List<LessonRecordResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .data(responseDTOS)
                .build();

        return ResponseEntity.ok(result);
    }
}
