package Capstone.VoQal.domain.lesson.note.repository;

import Capstone.VoQal.domain.lesson.note.domain.LessonNote;
import Capstone.VoQal.domain.lesson.note.dto.LessonNoteRequestDTO;
import Capstone.VoQal.domain.lesson.note.dto.UpdateLessonNoteDTO;

import java.util.List;

public interface LessonNoteRepositoryCustom {

    List<LessonNote> findNonDeletedByCoachIdAndStudentId(Long memberId,Long studentId);

    void deleteLessonNote(Long lessonNoteId);

    void updateLessonNote(Long lessonNoteId, UpdateLessonNoteDTO updateLessonNoteDTO);


}
