package Capstone.VoQal.domain.lesson.note.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLessonNote is a Querydsl query type for LessonNote
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLessonNote extends EntityPathBase<LessonNote> {

    private static final long serialVersionUID = -2016591670L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLessonNote lessonNote = new QLessonNote("lessonNote");

    public final Capstone.VoQal.global.domain.QBaseEntity _super = new Capstone.VoQal.global.domain.QBaseEntity(this);

    public final Capstone.VoQal.domain.member.domain.QCoachAndStudent coachAndStudent;

    public final StringPath contents = createString("contents");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final DatePath<java.time.LocalDate> lessonDate = createDate("lessonDate", java.time.LocalDate.class);

    public final StringPath lessonNoteTitle = createString("lessonNoteTitle");

    public final Capstone.VoQal.domain.lesson.record.domain.QLessonRecord lessonRecord;

    public final StringPath singer = createString("singer");

    public final StringPath songTitle = createString("songTitle");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QLessonNote(String variable) {
        this(LessonNote.class, forVariable(variable), INITS);
    }

    public QLessonNote(Path<? extends LessonNote> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLessonNote(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLessonNote(PathMetadata metadata, PathInits inits) {
        this(LessonNote.class, metadata, inits);
    }

    public QLessonNote(Class<? extends LessonNote> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.coachAndStudent = inits.isInitialized("coachAndStudent") ? new Capstone.VoQal.domain.member.domain.QCoachAndStudent(forProperty("coachAndStudent"), inits.get("coachAndStudent")) : null;
        this.lessonRecord = inits.isInitialized("lessonRecord") ? new Capstone.VoQal.domain.lesson.record.domain.QLessonRecord(forProperty("lessonRecord"), inits.get("lessonRecord")) : null;
    }

}

