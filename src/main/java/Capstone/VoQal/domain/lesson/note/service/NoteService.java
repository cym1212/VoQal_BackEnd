package Capstone.VoQal.domain.lesson.note.service;

import Capstone.VoQal.domain.lesson.note.domain.LessonNote;
import Capstone.VoQal.domain.lesson.note.dto.LessonNoteRequestDTO;
import Capstone.VoQal.domain.lesson.note.dto.LessonNoteResponseDTO;
import Capstone.VoQal.domain.lesson.note.dto.LessonNoteResponseDetailsDTO;
import Capstone.VoQal.domain.lesson.note.dto.UpdateLessonNoteDTO;
import Capstone.VoQal.domain.lesson.note.repository.LessonNoteRepository;
import Capstone.VoQal.domain.member.domain.Coach;
import Capstone.VoQal.domain.member.domain.Student;
import Capstone.VoQal.domain.member.service.MemberService;
import Capstone.VoQal.global.enums.ErrorCode;
import Capstone.VoQal.global.error.exception.BusinessException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteService {
    private final MemberService memberService;
    private final LessonNoteRepository lessonNoteRepository;



    @Transactional
    public List<LessonNoteResponseDTO> getAllLessonNote() {
        Coach currentCoach = memberService.getCurrentCoach();
        List<LessonNote> lessonNoteList = lessonNoteRepository.findNonDeletedByCoachId(currentCoach.getId());
        List<LessonNoteResponseDTO> responseDTOS = new ArrayList<>();
        for (LessonNote lessonNote : lessonNoteList) {
            responseDTOS.add(new LessonNoteResponseDTO(
                    lessonNote.getId(),
                    lessonNote.getLessonNoteTitle(),
                    lessonNote.getSongTitle(),
                    lessonNote.getSinger(),
                    lessonNote.getLessonDate()));
        }
        return responseDTOS;
    }

    @Transactional
    public LessonNoteResponseDetailsDTO getLessonNote(Long lessonNoteId) {

        LessonNote lessonNote = lessonNoteRepository.findById(lessonNoteId)
                .orElseThrow(() -> new BusinessException(ErrorCode.LESSONNOTE_NOT_FOUND));

        return LessonNoteResponseDetailsDTO.builder()
                .lessonNoteTitle(lessonNote.getLessonNoteTitle())
                .lessonDate(lessonNote.getLessonDate())
                .contents(lessonNote.getContents())
                .singer(lessonNote.getSinger())
                .songTitle(lessonNote.getSongTitle())
                .build();

    }

    @Transactional
    public void createLessonNote(LessonNoteRequestDTO lessonNoteRequestDTO) {
        Coach currentCoach = memberService.getCurrentCoach();
        Student assignedStudent = memberService.getCurrentStudent(lessonNoteRequestDTO.getStudentId());
        System.out.println("assignedStudent = " + assignedStudent);
        System.out.println("currentCoach = " + currentCoach);

        memberService.getCoachAndStudent(currentCoach.getId(), lessonNoteRequestDTO.getStudentId());

        LessonNote lessonNote = LessonNote.builder()
                .lessonNoteTitle(lessonNoteRequestDTO.getLessonNoteTitle())
                .lessonDate(lessonNoteRequestDTO.getLessonDate())
                .contents(lessonNoteRequestDTO.getContents())
                .singer(lessonNoteRequestDTO.getSinger())
                .songTitle(lessonNoteRequestDTO.getSongTitle())
                .coach(currentCoach)
                .student(assignedStudent)
                .build();

        lessonNoteRepository.save(lessonNote);

    }

    @Transactional
    public void updateLessonNote(Long lessonNoteId, UpdateLessonNoteDTO updateLessonNoteDTO) {
        lessonNoteRepository.updateLessonNote(lessonNoteId, updateLessonNoteDTO);

    }

    public void deleteLessonNote(Long lessonNoteId) {

        LessonNote lessonNote = lessonNoteRepository.findById(lessonNoteId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.LESSONNOTE_NOT_FOUND));
        lessonNoteRepository.deleteLessonNote(lessonNote.getId());
    }

}
