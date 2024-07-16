package Capstone.VoQal.domain.lesson.note.repository;

import Capstone.VoQal.domain.lesson.note.domain.LessonNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LessonNoteRepository extends JpaRepository<LessonNote, Long>, LessonNoteRepositoryCustom {

    Optional<LessonNote> findById(Long lessonNoteId);
}
