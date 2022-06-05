package core.join.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import core.join.entity.QJoinEntity;
import core.join.entity.RequesterType;
import core.join.entity.StatusType;
import core.player.entity.QPlayerEntity;
import core.team.entity.QTeamEntity;
import core.team.entity.TeamEntity;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JoinRepositoryImpl implements JoinRepositoryCustom{

	private final JPAQueryFactory queryFactory;
	
	QJoinEntity join = QJoinEntity.joinEntity;
	QPlayerEntity player = QPlayerEntity.playerEntity;
	QTeamEntity team = QTeamEntity.teamEntity;  
	
	@Override
	public List<Tuple> findPlayerJoinRequest(StatusType statusType, Long playerId,
			 Pageable pageable) {
		List<Tuple> proposals = queryFactory
				.select(team.teamEntity.teamName,join.joinEntity.statusType,join.joinEntity.createdDate)
				.from(join,team)
				.where(join.teamId.eq(team.id),join.activeYN.eq('Y'),eqPlayerId(playerId))
				.fetch();
				
		return proposals;
	}
	
	@Override
	public List<Tuple> findTeamJoinRequest(StatusType statusType, Long teamId, Pageable pageable) {
		List<Tuple> proposals = queryFactory
				.select(player.playerName,join.statusType,join.createdDate)
				.from(join,player)
				.where(join.playerId.eq(player.id),join.activeYN.eq('Y'),eqTeamId(teamId))
				.fetch();
		return null;
	}
	
	private BooleanExpression eqPlayerId(Long playerId) {
		if(playerId == null) {
			return null;
		}
		return join.playerId.eq(playerId);
	}
	
	private BooleanExpression eqTeamId(Long teamId) {
		if(teamId == null) {
			return null;
		}
		return join.teamId.eq(teamId);
	}

	

}
