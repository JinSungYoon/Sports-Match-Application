package core.player.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

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
	public List<PlayerEntity> findPlayer(String playerName, Integer uniformNo, String teamName,Pageable pageable) {
		List<PlayerEntity> players = queryFactory
				.select(player.playerEntity)
				.from(player.playerEntity)
				.join(player.team,team)
				.where(containsPlayerName(playerName),eqUniformNo(uniformNo),containsTeamName(teamName))
				.offset(pageable.getOffset())	// N번부터 시작
				.limit(pageable.getPageSize())	// 조회 갯수
				.orderBy(player.playerName.asc(),team.teamName.asc(),player.uniformNo.asc())
				.fetch();
		return players;
	}
	
	private BooleanExpression containsPlayerName(String playerName) {
		if(playerName == null || playerName.isEmpty()) {
			return null;
		}
		return player.playerName.contains(playerName);
	}
	
	private BooleanExpression eqUniformNo(Integer uniformNo) {
		if(uniformNo == null){
			return null;
		}
		return player.uniformNo.eq(uniformNo);
	}
	
	private BooleanExpression containsTeamName(String teamName) {
		if(teamName == null || teamName.isEmpty()) {
			return null;
		}
		return team.teamName.contains(teamName);
	}
	
}
