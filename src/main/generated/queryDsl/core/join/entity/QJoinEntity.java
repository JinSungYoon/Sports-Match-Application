package core.join.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QJoinEntity is a Querydsl query type for JoinEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QJoinEntity extends EntityPathBase<JoinEntity> {

    private static final long serialVersionUID = -1093435357L;

    public static final QJoinEntity joinEntity = new QJoinEntity("joinEntity");

    public final core.common.entity.QBaseEntity _super = new core.common.entity.QBaseEntity(this);

    public final ComparablePath<Character> activeYN = createComparable("activeYN", Character.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> joinId = createNumber("joinId", Long.class);

    public final NumberPath<Long> playerId = createNumber("playerId", Long.class);

    public final EnumPath<RequesterType> requesterType = createEnum("requesterType", RequesterType.class);

    public final EnumPath<StatusType> statusType = createEnum("statusType", StatusType.class);

    public final NumberPath<Long> teamId = createNumber("teamId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDate = _super.updatedDate;

    public QJoinEntity(String variable) {
        super(JoinEntity.class, forVariable(variable));
    }

    public QJoinEntity(Path<? extends JoinEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QJoinEntity(PathMetadata metadata) {
        super(JoinEntity.class, metadata);
    }

}

