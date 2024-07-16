package Capstone.VoQal.domain.lesson.note.controller;

import Capstone.VoQal.domain.lesson.note.dto.LessonNoteRequestDTO;
import Capstone.VoQal.domain.lesson.note.dto.LessonNoteResponseDTO;
import Capstone.VoQal.domain.lesson.note.dto.LessonNoteResponseDetailsDTO;
import Capstone.VoQal.domain.lesson.note.dto.UpdateLessonNoteDTO;
import Capstone.VoQal.domain.lesson.note.service.NoteService;
import Capstone.VoQal.global.dto.MessageDTO;
import Capstone.VoQal.global.dto.ResponseWrapper;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "레슨 노트 작성 ", description = "담당학생의 레슨 노트를 작성합니다.")
    public ResponseEntity<MessageDTO> createLessonNote(LessonNoteRequestDTO lessonNoteRequestDTO) {
        noteService.createLessonNote(lessonNoteRequestDTO);
        return ResponseEntity.ok().body(MessageDTO.builder()
                .status(HttpStatus.OK.value())
                .message("성공적으로 작성되었습니다")
                .build());
    }

    @GetMapping("/lessonnote")
    @Operation(summary = "레슨 노트 전체 조회 ", description = "담당학생의 레슨 노트를 전체 조회합니다.")
    public ResponseEntity<ResponseWrapper<List<LessonNoteResponseDTO>>> getAllLessonNote() {
        List<LessonNoteResponseDTO> responseDTOS = noteService.getAllLessonNote();
        ResponseWrapper<List<LessonNoteResponseDTO>> result = ResponseWrapper.<List<LessonNoteResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .data(responseDTOS)
                .build();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/lessonnote/{lessonnoteId}")
    @Operation(summary = "레슨 노트 상세 조회 ", description = "담당학생의 레슨 노트를 상세 조회합니다.")
    public ResponseEntity<LessonNoteResponseDetailsDTO> getLessonNote(@PathVariable("lessonnoteId") Long lessonnoteId) {
        LessonNoteResponseDetailsDTO detailsDTO = noteService.getLessonNote(lessonnoteId);
        return ResponseEntity.ok(detailsDTO);
    }

    @PatchMapping("/lessonnote/{lessonnoteId}")
    @Operation(summary = "레슨 노트 수정 ", description = "담당학생의 레슨 노트를 수정합니다.")
    public ResponseEntity<MessageDTO> updateLessonNote(@PathVariable("lessonnoteId") Long lessonnoteId, @RequestBody UpdateLessonNoteDTO updateLessonNoteDTO) {
        noteService.updateLessonNote(lessonnoteId, updateLessonNoteDTO);
        return ResponseEntity.ok().body(MessageDTO.builder()
                .status(HttpStatus.OK.value())
                .message("성공적으로 수정되었습니다")
                .build());
    }


    @DeleteMapping("/lessonnote/{lessonnoteId}")
    @Operation(summary = "레슨 노트 삭제 ", description = "담당학생의 레슨 노트를 삭제합니다.")
    public ResponseEntity<MessageDTO> deleteLessonNote (@PathVariable("lessonnoteId") Long lessonnoteId) {
        noteService.deleteLessonNote(lessonnoteId);
        return ResponseEntity.ok().body(MessageDTO.builder()
                .status(HttpStatus.OK.value())
                .message("성공적으로 삭제되었습니다")
                .build());
    }




}
