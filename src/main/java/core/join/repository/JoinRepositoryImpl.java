package core.join.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import core.join.dto.JoinDto;
import core.join.entity.QJoinEntity;
import core.join.entity.StatusType;
import core.player.entity.QPlayerEntity;
import core.team.entity.QTeamEntity;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JoinRepositoryImpl implements JoinRepositoryCustom{

	private final JPAQueryFactory queryFactory;
	
	QJoinEntity join = QJoinEntity.joinEntity;
	QPlayerEntity player = QPlayerEntity.playerEntity;
	QTeamEntity team = QTeamEntity.teamEntity;  
	
	@Override
	public List<JoinDto> findPlayerJoinRequest(StatusType statusType,Long playerId,Long teamId,Pageable pageable) {
		List<JoinDto> proposals = queryFactory
				.select(Projections.fields(JoinDto.class,join.joinId,join.team.id.as("teamId"),join.player.id.as("playerId"),join.requesterType,join.statusType,join.activeYN,join.createdDate,join.updatedDate))
				.from(join)
				.join(join.team,team)
				.where(join.activeYN.eq('Y'),eqPlayerId(playerId),eqTeamId(teamId))
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.orderBy(join.joinId.asc(),join.updatedDate.desc())
				.fetch();
		
		return proposals;
	}
	
	@Override
	public List<JoinDto> findTeamJoinRequest(StatusType statusType,Long playerId ,Long teamId, Pageable pageable) {
		List<JoinDto> proposals = queryFactory
				.select(Projections.fields(JoinDto.class,join.joinId,join.team.id.as("teamId"),join.player.id.as("playerId"),join.requesterType,join.statusType,join.activeYN,join.createdDate,join.updatedDate))
				.from(join)
				.join(join.player,player)
				.where(join.activeYN.eq('Y'),eqPlayerId(playerId),eqTeamId(teamId))
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.orderBy(join.joinId.asc(),join.updatedDate.desc())
				.fetch();
		return proposals;
	}
	
	private BooleanExpression eqPlayerId(Long playerId) {
		if(playerId == null) {
			return null;
		}
		return join.player.id.eq(playerId);
	}
	
	private BooleanExpression eqTeamId(Long teamId) {
		if(teamId == null) {
			return null;
		}
		return join.team.id.eq(teamId);
	}

	

}
