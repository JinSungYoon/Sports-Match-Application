package core.player.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPlayerEntity is a Querydsl query type for PlayerEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPlayerEntity extends EntityPathBase<PlayerEntity> {

    private static final long serialVersionUID = 1761456017L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPlayerEntity playerEntity = new QPlayerEntity("playerEntity");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath playerName = createString("playerName");

    public final StringPath resRegNo = createString("resRegNo");

    public final core.team.entity.QTeamEntity team;

    public final NumberPath<Integer> uniformNo = createNumber("uniformNo", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDate = _super.updatedDate;

    public QPlayerEntity(String variable) {
        this(PlayerEntity.class, forVariable(variable), INITS);
    }

    public QPlayerEntity(Path<? extends PlayerEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPlayerEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPlayerEntity(PathMetadata metadata, PathInits inits) {
        this(PlayerEntity.class, metadata, inits);
    }

    public QPlayerEntity(Class<? extends PlayerEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.team = inits.isInitialized("team") ? new core.team.entity.QTeamEntity(forProperty("team")) : null;
    }

}

