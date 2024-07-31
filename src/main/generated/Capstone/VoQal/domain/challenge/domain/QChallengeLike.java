package Capstone.VoQal.domain.challenge.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChallengeLike is a Querydsl query type for ChallengeLike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChallengeLike extends EntityPathBase<ChallengeLike> {

    private static final long serialVersionUID = 1710340479L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChallengeLike challengeLike = new QChallengeLike("challengeLike");

    public final Capstone.VoQal.global.domain.QBaseEntity _super = new Capstone.VoQal.global.domain.QBaseEntity(this);

    public final QChallengePost challengePost;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final Capstone.VoQal.domain.member.domain.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QChallengeLike(String variable) {
        this(ChallengeLike.class, forVariable(variable), INITS);
    }

    public QChallengeLike(Path<? extends ChallengeLike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChallengeLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChallengeLike(PathMetadata metadata, PathInits inits) {
        this(ChallengeLike.class, metadata, inits);
    }

    public QChallengeLike(Class<? extends ChallengeLike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.challengePost = inits.isInitialized("challengePost") ? new QChallengePost(forProperty("challengePost"), inits.get("challengePost")) : null;
        this.member = inits.isInitialized("member") ? new Capstone.VoQal.domain.member.domain.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

