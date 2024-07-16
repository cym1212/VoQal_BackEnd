package Capstone.VoQal.domain.lesson.record.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecord is a Querydsl query type for Record
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecord extends EntityPathBase<Record> {

    private static final long serialVersionUID = -1136071312L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecord record = new QRecord("record");

    public final Capstone.VoQal.global.domain.QBaseEntity _super = new Capstone.VoQal.global.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final Capstone.VoQal.domain.lesson.note.domain.QLessonNote lessonNote;

    public final StringPath recordUrl = createString("recordUrl");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QRecord(String variable) {
        this(Record.class, forVariable(variable), INITS);
    }

    public QRecord(Path<? extends Record> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecord(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecord(PathMetadata metadata, PathInits inits) {
        this(Record.class, metadata, inits);
    }

    public QRecord(Class<? extends Record> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.lessonNote = inits.isInitialized("lessonNote") ? new Capstone.VoQal.domain.lesson.note.domain.QLessonNote(forProperty("lessonNote"), inits.get("lessonNote")) : null;
    }

}

