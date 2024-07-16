package Capstone.VoQal.domain.lesson.note.repository;

import Capstone.VoQal.domain.lesson.note.domain.LessonNote;
import Capstone.VoQal.domain.lesson.note.domain.QLessonNote;
import Capstone.VoQal.domain.lesson.note.dto.UpdateLessonNoteDTO;
import Capstone.VoQal.global.enums.ErrorCode;
import Capstone.VoQal.global.error.exception.BusinessException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static Capstone.VoQal.domain.lesson.note.domain.QLessonNote.lessonNote;

@Repository
@RequiredArgsConstructor
public class LessonNoteRepositoryCustomImpl implements LessonNoteRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public List<LessonNote> findNonDeletedByCoachId(Long coachId) {
        return queryFactory.selectFrom(lessonNote)
                .where(
                        lessonNote.coach.id.eq(coachId)
                                .and(lessonNote.deletedAt.isNull())
                )
                .fetch();
    }

    @Transactional
    @Override
    public void deleteLessonNote(Long lessonNoteId) {
        long updatedCount = queryFactory.update(lessonNote)
                .set(lessonNote.deletedAt, LocalDateTime.now())
                .where(lessonNote.id.eq(lessonNoteId))
                .execute();

        if (updatedCount == 0) {
            throw new BusinessException(ErrorCode.LESSONNOTE_NOT_FOUND_OR_ALREADY_DELETED);
        }
    }
    @Transactional
    @Override
    public void updateLessonNote(Long lessonNoteId, UpdateLessonNoteDTO updateLessonNoteDTO) {
        LessonNote existingLessonNote = entityManager.find(LessonNote.class, lessonNoteId);
        if (existingLessonNote == null) {
            throw new BusinessException(ErrorCode.LESSONNOTE_NOT_FOUND);
        }

        long updatedCount = queryFactory.update(lessonNote)
                .set(lessonNote.lessonNoteTitle, updateLessonNoteDTO.getUpdateLessonNoteTitle() != null ? updateLessonNoteDTO.getUpdateLessonNoteTitle() : existingLessonNote.getLessonNoteTitle())
                .set(lessonNote.lessonDate, updateLessonNoteDTO.getUpdateLessonDate() != null ? updateLessonNoteDTO.getUpdateLessonDate() : existingLessonNote.getLessonDate())
                .set(lessonNote.contents, updateLessonNoteDTO.getUpdateContents() != null ? updateLessonNoteDTO.getUpdateContents() : existingLessonNote.getContents())
                .set(lessonNote.singer, updateLessonNoteDTO.getUpdateSinger() != null ? updateLessonNoteDTO.getUpdateSinger() : existingLessonNote.getSinger())
                .set(lessonNote.songTitle, updateLessonNoteDTO.getUpdateSongTitle() != null ? updateLessonNoteDTO.getUpdateSongTitle() : existingLessonNote.getSongTitle())
                .where(lessonNote.id.eq(lessonNoteId))
                .execute();

        if (updatedCount == 0) {
            throw new BusinessException(ErrorCode.LESSONNOTE_NOT_UPDATED);
        }

    }
}
