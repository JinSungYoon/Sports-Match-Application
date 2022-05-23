package core.team.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTeamEntity is a Querydsl query type for TeamEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTeamEntity extends EntityPathBase<TeamEntity> {

    private static final long serialVersionUID = -1013149303L;

    public static final QTeamEntity teamEntity = new QTeamEntity("teamEntity");

    public final core.player.entity.QBaseEntity _super = new core.player.entity.QBaseEntity(this);

    public final EnumPath<core.player.entity.BelongType> belongType = createEnum("belongType", core.player.entity.BelongType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath introduction = createString("introduction");

    public final StringPath location = createString("location");

    public final StringPath teamName = createString("teamName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDate = _super.updatedDate;

    public QTeamEntity(String variable) {
        super(TeamEntity.class, forVariable(variable));
    }

    public QTeamEntity(Path<? extends TeamEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTeamEntity(PathMetadata metadata) {
        super(TeamEntity.class, metadata);
    }

}

