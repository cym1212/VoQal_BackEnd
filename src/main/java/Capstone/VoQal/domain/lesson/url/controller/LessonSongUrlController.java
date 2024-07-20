package Capstone.VoQal.domain.lesson.url.controller;

import Capstone.VoQal.domain.lesson.url.dto.GetLessonSongRequestDTO;
import Capstone.VoQal.domain.lesson.url.dto.LessonSongResponseDTO;
import Capstone.VoQal.domain.lesson.url.dto.SetLessonSongUrlDTO;
import Capstone.VoQal.domain.lesson.url.dto.UpdateLessonSongUrlDTO;
import Capstone.VoQal.domain.lesson.url.service.LessonSongUrlService;
import Capstone.VoQal.global.dto.MessageDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LessonSongUrlController {

    private final LessonSongUrlService lessonSongUrlService;

    @GetMapping("/lessonsongurl")
    @Operation(summary = "url조회 로직", description = "담당 학생의 url을 조회합니다.")
    public ResponseEntity<LessonSongResponseDTO> getLessonSongUrl(GetLessonSongRequestDTO getLessonSongRequestDTO) {
        LessonSongResponseDTO lessonSongUrl = lessonSongUrlService.getLessonSongUrl(getLessonSongRequestDTO);
        return ResponseEntity.ok(lessonSongUrl);
    }

    @PostMapping("/lessonsongurl")
    @Operation(summary = "url 입력 로직", description = "담당학생의 url을 입력합니다.")
    public ResponseEntity<LessonSongResponseDTO> createLessonSongUrl(@RequestBody SetLessonSongUrlDTO setLessonSongUrlDTO) {
        LessonSongResponseDTO responseDTO = lessonSongUrlService.createLessonSongUrl(setLessonSongUrlDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("/lessonsongurl/{studentId}")
    @Operation(summary = "url 수정 로직", description = "담당학생의 url을 수정합니다.")
    public ResponseEntity<LessonSongResponseDTO> updateLessonSongUrl(@PathVariable("studentId") Long studentId, @RequestBody UpdateLessonSongUrlDTO updateLessonSongUrlDTO) {
        LessonSongResponseDTO responseDTO = lessonSongUrlService.updateLessonSongUrl(studentId, updateLessonSongUrlDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/lessonsongurl/{studentId}")
    @Operation(summary = "url 삭제 로직", description = "담당학생의 url을 삭제합니다.")
    public ResponseEntity<MessageDTO> deleteLessonSongUrl(@PathVariable("studentId") Long studentId) {
        lessonSongUrlService.deleteLessonSongUrl(studentId);


        return ResponseEntity.ok().body(MessageDTO.builder().status(200).message("성공적으로 삭제되었습니다.").build());
    }

    @GetMapping("/lessonsongurl/student")
    @Operation(summary = "학생입장 - url 조회 로직", description = "담당 코치가 등록한 url을 조회합니다.")
    public ResponseEntity<LessonSongResponseDTO> getLessonSongUrlForStudent() {
        LessonSongResponseDTO lessonSongUrlForStudent = lessonSongUrlService.getLessonSongUrlForStudent();
        return ResponseEntity.ok(lessonSongUrlForStudent);
    }
}
