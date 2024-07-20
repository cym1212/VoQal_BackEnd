package Capstone.VoQal.domain.lesson.url.controller;

import Capstone.VoQal.domain.lesson.url.dto.GetLessonSongRequestDTO;
import Capstone.VoQal.domain.lesson.url.dto.LessonSongResponseDTO;
import Capstone.VoQal.domain.lesson.url.dto.SetLessonSongUrlDTO;
import Capstone.VoQal.domain.lesson.url.dto.UpdateLessonSongUrlDTO;
import Capstone.VoQal.domain.lesson.url.service.LessonSongUrlService;
import Capstone.VoQal.global.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LessonSongUrlController {

    private final LessonSongUrlService lessonSongUrlService;

    @GetMapping("/lessonsongurl")
    public ResponseEntity<LessonSongResponseDTO> getLessonSongUrl(GetLessonSongRequestDTO getLessonSongRequestDTO) {
        LessonSongResponseDTO lessonSongUrl = lessonSongUrlService.getLessonSongUrl(getLessonSongRequestDTO);
        return ResponseEntity.ok(lessonSongUrl);
    }

    @PostMapping("/lessonsongurl")
    public ResponseEntity<LessonSongResponseDTO> createLessonSongUrl(@RequestBody SetLessonSongUrlDTO setLessonSongUrlDTO) {
        LessonSongResponseDTO responseDTO = lessonSongUrlService.createLessonSongUrl(setLessonSongUrlDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("/lessonsongurl/{studentId}")
    public ResponseEntity<LessonSongResponseDTO> updateLessonSongUrl(@PathVariable("studentId") Long studentId, @RequestBody UpdateLessonSongUrlDTO updateLessonSongUrlDTO) {
        LessonSongResponseDTO responseDTO = lessonSongUrlService.updateLessonSongUrl(studentId, updateLessonSongUrlDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/lessonsongurl/{studentId}")
    public ResponseEntity<MessageDTO> deleteLessonSongUrl(@PathVariable("studentId") Long studentId) {
        lessonSongUrlService.deleteLessonSongUrl(studentId);


        return ResponseEntity.ok().body(MessageDTO.builder().status(200).message("성공적으로 삭제되었습니다.").build());
    }

    @GetMapping("/lessonsongurl/student")
    public ResponseEntity<LessonSongResponseDTO> getLessonSongUrlForStudent() {
        LessonSongResponseDTO lessonSongUrlForStudent = lessonSongUrlService.getLessonSongUrlForStudent();
        return ResponseEntity.ok(lessonSongUrlForStudent);
    }
}
