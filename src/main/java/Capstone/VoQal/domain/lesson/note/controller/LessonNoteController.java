package Capstone.VoQal.domain.lesson.note.controller;

import Capstone.VoQal.domain.lesson.note.dto.*;
import Capstone.VoQal.domain.lesson.note.service.NoteService;
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
public class LessonNoteController {

    private final NoteService noteService;


    @PostMapping("/create/note")
    @Operation(summary = "레슨 노트 작성 ", description = "담당학생의 레슨 노트를 작성합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "400", description = "코치가 아닙니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
    public ResponseEntity<MessageDTO> createLessonNote(@RequestBody LessonNoteRequestDTO lessonNoteRequestDTO) {
        noteService.createLessonNoteByCoach(lessonNoteRequestDTO);
        return ResponseEntity.ok().body(MessageDTO.builder()
                .status(HttpStatus.OK.value())
                .message("성공적으로 작성되었습니다")
                .build());
    }

    @GetMapping("/lessonNote")
    @Operation(summary = "레슨 노트 전체 조회 ", description = "담당학생의 레슨 노트를 전체 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "400", description = "코치가 아닙니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
    public ResponseEntity<ResponseWrapper<List<LessonNoteResponseDTO>>> getAllLessonNote(@ModelAttribute GetLessonNoteDTO getLessonNoteDTO) {
        List<LessonNoteResponseDTO> responseDTOS = noteService.getAllLessonNoteByCoach(getLessonNoteDTO);
        ResponseWrapper<List<LessonNoteResponseDTO>> result = ResponseWrapper.<List<LessonNoteResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .data(responseDTOS)
                .build();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/lessonNote/{lessonNoteId}")
    @Operation(summary = "레슨 노트 상세 조회 ", description = "담당학생의 레슨 노트를 상세 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "400", description = "레슨노트를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
    public ResponseEntity<ResponseWrapper<LessonNoteResponseDetailsDTO>> getLessonNote(@PathVariable("lessonNoteId") Long lessonnoteId) {
        LessonNoteResponseDetailsDTO detailsDTO = noteService.getLessonNoteDetails(lessonnoteId);
        ResponseWrapper<LessonNoteResponseDetailsDTO> result = ResponseWrapper.<LessonNoteResponseDetailsDTO>builder()
                .status(HttpStatus.OK.value())
                .data(detailsDTO)
                .build();
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/lessonNote/{lessonNoteId}")
    @Operation(summary = "레슨 노트 수정 ", description = "담당학생의 레슨 노트를 수정합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "400", description = "레슨노트를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "400", description = "레슨노트가 변경되지 않았습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
    public ResponseEntity<MessageDTO> updateLessonNote(@PathVariable("lessonNoteId") Long lessonnoteId, @RequestBody UpdateLessonNoteDTO updateLessonNoteDTO) {
        noteService.updateLessonNoteByCoach(lessonnoteId, updateLessonNoteDTO);
        return ResponseEntity.ok().body(MessageDTO.builder()
                .status(HttpStatus.OK.value())
                .message("성공적으로 수정되었습니다")
                .build());
    }


    @DeleteMapping("/lessonNote/{lessonNoteId}")
    @Operation(summary = "레슨 노트 삭제 ", description = "담당학생의 레슨 노트를 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "400", description = "레슨노트를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "400", description = "레슨노트를 찾을 수 없거나 이미 삭제된 상태입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
    public ResponseEntity<MessageDTO> deleteLessonNote (@PathVariable("lessonNoteId") Long lessonnoteId) {
        noteService.deleteLessonNote(lessonnoteId);
        return ResponseEntity.ok().body(MessageDTO.builder()
                .status(HttpStatus.OK.value())
                .message("성공적으로 삭제되었습니다")
                .build());
    }

    @GetMapping("/lessonNote/student")
    @Operation(summary = "레슨 노트 전체 조회 - 학생입장 ", description = "담당코치가 작성한 레슨 노트를 전체 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 멤버 ID 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "400", description = "코치를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
    public ResponseEntity<ResponseWrapper<List<LessonNoteResponseDTO>>> getAllLessonNote() {
        List<LessonNoteResponseDTO> responseDTOS = noteService.getAllLessonNoteForStudent();
        ResponseWrapper<List<LessonNoteResponseDTO>> result = ResponseWrapper.<List<LessonNoteResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .data(responseDTOS)
                .build();

        return ResponseEntity.ok(result);
    }


}
