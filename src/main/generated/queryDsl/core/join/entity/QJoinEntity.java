package core.join.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QJoinEntity is a Querydsl query type for JoinEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QJoinEntity extends EntityPathBase<JoinEntity> {

    private static final long serialVersionUID = -1093435357L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QJoinEntity joinEntity = new QJoinEntity("joinEntity");

    public final core.common.entity.QBaseEntity _super = new core.common.entity.QBaseEntity(this);

    public final ComparablePath<Character> activeYN = createComparable("activeYN", Character.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> joinId = createNumber("joinId", Long.class);

    public final core.player.entity.QPlayerEntity player;

    public final EnumPath<RequesterType> requesterType = createEnum("requesterType", RequesterType.class);

    public final EnumPath<StatusType> statusType = createEnum("statusType", StatusType.class);

    public final core.team.entity.QTeamEntity team;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDate = _super.updatedDate;

    public QJoinEntity(String variable) {
        this(JoinEntity.class, forVariable(variable), INITS);
    }

    public QJoinEntity(Path<? extends JoinEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QJoinEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QJoinEntity(PathMetadata metadata, PathInits inits) {
        this(JoinEntity.class, metadata, inits);
    }

    public QJoinEntity(Class<? extends JoinEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.player = inits.isInitialized("player") ? new core.player.entity.QPlayerEntity(forProperty("player"), inits.get("player")) : null;
        this.team = inits.isInitialized("team") ? new core.team.entity.QTeamEntity(forProperty("team")) : null;
    }

}

