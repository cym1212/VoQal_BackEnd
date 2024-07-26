package Capstone.VoQal.domain.lesson.record.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLessonRecord is a Querydsl query type for LessonRecord
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLessonRecord extends EntityPathBase<LessonRecord> {

    private static final long serialVersionUID = -2107496056L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLessonRecord lessonRecord = new QLessonRecord("lessonRecord");

    public final Capstone.VoQal.global.domain.QBaseEntity _super = new Capstone.VoQal.global.domain.QBaseEntity(this);

    public final Capstone.VoQal.domain.member.domain.QCoachAndStudent coachAndStudent;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final Capstone.VoQal.domain.lesson.note.domain.QLessonNote lessonNote;

    public final DatePath<java.time.LocalDate> recordDate = createDate("recordDate", java.time.LocalDate.class);

    public final StringPath recordTitle = createString("recordTitle");

    public final StringPath recordUrl = createString("recordUrl");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QLessonRecord(String variable) {
        this(LessonRecord.class, forVariable(variable), INITS);
    }

    public QLessonRecord(Path<? extends LessonRecord> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLessonRecord(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLessonRecord(PathMetadata metadata, PathInits inits) {
        this(LessonRecord.class, metadata, inits);
    }

    public QLessonRecord(Class<? extends LessonRecord> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.coachAndStudent = inits.isInitialized("coachAndStudent") ? new Capstone.VoQal.domain.member.domain.QCoachAndStudent(forProperty("coachAndStudent"), inits.get("coachAndStudent")) : null;
        this.lessonNote = inits.isInitialized("lessonNote") ? new Capstone.VoQal.domain.lesson.note.domain.QLessonNote(forProperty("lessonNote"), inits.get("lessonNote")) : null;
    }

}

