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

    public final Capstone.VoQal.domain.member.domain.QCoach coach;

    public final StringPath contents = createString("contents");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final DatePath<java.time.LocalDate> lessonDate = createDate("lessonDate", java.time.LocalDate.class);

    public final StringPath lessonNoteTitle = createString("lessonNoteTitle");

    public final Capstone.VoQal.domain.lesson.record.domain.QRecord record;

    public final StringPath singer = createString("singer");

    public final StringPath songTitle = createString("songTitle");

    public final Capstone.VoQal.domain.member.domain.QStudent student;

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
        this.coach = inits.isInitialized("coach") ? new Capstone.VoQal.domain.member.domain.QCoach(forProperty("coach"), inits.get("coach")) : null;
        this.record = inits.isInitialized("record") ? new Capstone.VoQal.domain.lesson.record.domain.QRecord(forProperty("record"), inits.get("record")) : null;
        this.student = inits.isInitialized("student") ? new Capstone.VoQal.domain.member.domain.QStudent(forProperty("student"), inits.get("student")) : null;
    }

}

