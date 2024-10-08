package Capstone.VoQal.domain.lesson.note.service;

import Capstone.VoQal.domain.lesson.note.domain.LessonNote;
import Capstone.VoQal.domain.lesson.note.dto.*;
import Capstone.VoQal.domain.lesson.note.repository.LessonNoteRepository;
import Capstone.VoQal.domain.member.domain.CoachAndStudent;
import Capstone.VoQal.domain.member.domain.Member;
import Capstone.VoQal.domain.member.repository.CoachAndStudent.CoachAndStudentRepository;
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


    //coach
    @Transactional
    public List<LessonNoteResponseDTO> getAllLessonNoteByCoach(GetLessonNoteDTO getLessonNoteDTO) {
        Member currentCoach = memberService.getCurrentCoach();

        List<LessonNote> lessonNoteList = lessonNoteRepository.findNonDeletedNoteByCoachIdAndStudentId(currentCoach.getId(),getLessonNoteDTO.getStudentId());
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

    //coach, student
    @Transactional
    public LessonNoteResponseDetailsDTO getLessonNoteDetails(Long lessonNoteId) {

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
    //coach
    @Transactional
    public void createLessonNoteByCoach(LessonNoteRequestDTO lessonNoteRequestDTO) {
        Member currentCoach = memberService.getCurrentCoach();

        CoachAndStudent coachAndStudent = memberService.getCoachAndStudent(currentCoach.getId(), lessonNoteRequestDTO.getStudentId());

        LessonNote lessonNote = LessonNote.builder()
                .lessonNoteTitle(lessonNoteRequestDTO.getLessonNoteTitle())
                .lessonDate(lessonNoteRequestDTO.getLessonDate())
                .contents(lessonNoteRequestDTO.getContents())
                .singer(lessonNoteRequestDTO.getSinger())
                .songTitle(lessonNoteRequestDTO.getSongTitle())
                .coachAndStudent(coachAndStudent)
                .build();

        lessonNoteRepository.save(lessonNote);

    }


    //coach
    @Transactional
    public void updateLessonNoteByCoach(Long lessonNoteId, UpdateLessonNoteDTO updateLessonNoteDTO) {
        lessonNoteRepository.updateLessonNote(lessonNoteId, updateLessonNoteDTO);

    }

    //coach, student
    @Transactional
    public void deleteLessonNote(Long lessonNoteId) {

        LessonNote lessonNote = lessonNoteRepository.findById(lessonNoteId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.LESSONNOTE_NOT_FOUND));
        lessonNoteRepository.deleteLessonNote(lessonNote.getId());
    }

    //student
    @Transactional
    public List<LessonNoteResponseDTO> getAllLessonNoteForStudent() {
        Member currentStudent = memberService.getCurrentMember();
        Long coach = memberService.getCoachIdByStudentId(currentStudent.getId());

        List<LessonNote> lessonNoteList = lessonNoteRepository.findNonDeletedNoteByCoachIdAndStudentId(coach,currentStudent.getId());
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



}
