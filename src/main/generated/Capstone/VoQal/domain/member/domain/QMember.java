package Capstone.VoQal.domain.member.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 8676820L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMember member = new QMember("member1");

    public final Capstone.VoQal.global.domain.QBaseEntity _super = new Capstone.VoQal.global.domain.QBaseEntity(this);

    public final SetPath<Capstone.VoQal.domain.challenge.domain.ChallengeLike, Capstone.VoQal.domain.challenge.domain.QChallengeLike> challengeLikes = this.<Capstone.VoQal.domain.challenge.domain.ChallengeLike, Capstone.VoQal.domain.challenge.domain.QChallengeLike>createSet("challengeLikes", Capstone.VoQal.domain.challenge.domain.ChallengeLike.class, Capstone.VoQal.domain.challenge.domain.QChallengeLike.class, PathInits.DIRECT2);

    public final SetPath<Capstone.VoQal.domain.challenge.domain.ChallengePost, Capstone.VoQal.domain.challenge.domain.QChallengePost> challengePost = this.<Capstone.VoQal.domain.challenge.domain.ChallengePost, Capstone.VoQal.domain.challenge.domain.QChallengePost>createSet("challengePost", Capstone.VoQal.domain.challenge.domain.ChallengePost.class, Capstone.VoQal.domain.challenge.domain.QChallengePost.class, PathInits.DIRECT2);

    public final QCoach coach;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final StringPath email = createString("email");

    public final StringPath fcmToken = createString("fcmToken");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath name = createString("name");

    public final StringPath nickName = createString("nickName");

    public final StringPath password = createString("password");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final StringPath refreshToken = createString("refreshToken");

    public final ListPath<Capstone.VoQal.domain.reservation.domain.Reservation, Capstone.VoQal.domain.reservation.domain.QReservation> reservations = this.<Capstone.VoQal.domain.reservation.domain.Reservation, Capstone.VoQal.domain.reservation.domain.QReservation>createList("reservations", Capstone.VoQal.domain.reservation.domain.Reservation.class, Capstone.VoQal.domain.reservation.domain.QReservation.class, PathInits.DIRECT2);

    public final EnumPath<Capstone.VoQal.global.enums.Role> role = createEnum("role", Capstone.VoQal.global.enums.Role.class);

    public final QStudent student;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMember(String variable) {
        this(Member.class, forVariable(variable), INITS);
    }

    public QMember(Path<? extends Member> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMember(PathMetadata metadata, PathInits inits) {
        this(Member.class, metadata, inits);
    }

    public QMember(Class<? extends Member> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.coach = inits.isInitialized("coach") ? new QCoach(forProperty("coach"), inits.get("coach")) : null;
        this.student = inits.isInitialized("student") ? new QStudent(forProperty("student"), inits.get("student")) : null;
    }

}

