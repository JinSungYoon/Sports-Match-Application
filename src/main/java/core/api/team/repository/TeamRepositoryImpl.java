package core.api.team.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import core.api.player.entity.BelongType;
import core.api.team.entity.TeamEntity;
import core.team.entity.QTeamEntity;

public class TeamRepositoryImpl implements TeamRepositoryCustom {

	private final JPAQueryFactory queryFactory;
	
	public TeamRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}
	
	QTeamEntity team = QTeamEntity.teamEntity;
	
	@Override
	public List<TeamEntity> findTeam(String teamName,String location,BelongType belongType,String introduction,Pageable pageable) {
		List<TeamEntity> teams = queryFactory
				.select(team.teamEntity)
				.from(team.teamEntity)
				.where(eqTeamName(teamName),containLocation(location),eqBelongType(belongType),containIntroduction(introduction))
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.orderBy(team.id.asc(),team.location.asc(),team.belongType.asc())
				.fetch();
				
		return teams;
	}
	
	private BooleanExpression eqTeamName(String teamName) {
		if(teamName == null || teamName.isEmpty()) {
			return null;
		}
		return team.teamName.eq(teamName);
	}
	
	private BooleanExpression containLocation(String location) {
		if(location == null || location.isEmpty()) {
			return null;
		}
		return team.location.contains(location);
	}
	
	private BooleanExpression eqBelongType(BelongType belongType) {
		if(belongType == null) {
			return null;
		}
		return team.belongType.eq(belongType);
	}
	
	private BooleanExpression containIntroduction(String introduction) {
		if(introduction == null || introduction.isEmpty()) {
			return null;
		}
		return team.introduction.contains(introduction);
	}
}
