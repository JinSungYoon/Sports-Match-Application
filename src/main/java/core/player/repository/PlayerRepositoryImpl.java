package core.player.repository;

import java.util.List;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import core.player.entity.PlayerEntity;
import core.player.entity.QPlayerEntity;
import core.team.entity.QTeamEntity;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlayerRepositoryImpl implements PlayerRepositoryCustom {

	private final JPAQueryFactory queryFactory;
	
	QPlayerEntity player = QPlayerEntity.playerEntity;
	QTeamEntity team = QTeamEntity.teamEntity;
	
	@Override
	public List<PlayerEntity> findPlayer(String playerName, Integer uniformNo, String teamName) {
		List<PlayerEntity> players = queryFactory
				.select(player.playerEntity)
				.from(player.playerEntity)
				.join(player.team,team)
				.where(eqPlayerName(playerName),eqUniformNo(uniformNo),eqTeamName(teamName))
				.orderBy(player.playerName.asc(),team.teamName.asc(),player.uniformNo.asc())
				.fetch();
		return players;
	}
	
	private BooleanExpression eqPlayerName(String playerName) {
		if(playerName == null || playerName.isEmpty()) {
			return null;
		}
		return player.playerName.eq(playerName);
	}
	
	private BooleanExpression eqUniformNo(Integer uniformNo) {
		if(uniformNo == null){
			return null;
		}
		return player.uniformNo.eq(uniformNo);
	}
	
	private BooleanExpression eqTeamName(String teamName) {
		if(teamName == null || teamName.isEmpty()) {
			return null;
		}
		return team.teamName.eq(teamName);
	}
	
}
