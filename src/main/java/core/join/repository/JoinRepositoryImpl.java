package core.join.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import core.join.dto.JoinDto;
import core.join.entity.QJoinEntity;
import core.join.entity.RequesterType;
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
	public Page<JoinDto> findPlayerJoinApplication(StatusType statusType,Long playerId,Pageable pageable) {
		QueryResults<JoinDto> results = queryFactory
				.select(Projections.fields(JoinDto.class,join.id,join.player.id.as("playerId"),join.player.playerName,join.team.id.as("teamId"),join.team.teamName,join.requesterType,join.statusType,join.activeYN,join.createdDate,join.updatedDate))
				.from(join)
				.join(join.player,player)
				.where(join.requesterType.eq(RequesterType.PLAYER),eqStatusType(statusType),eqPlayerId(playerId),join.activeYN.eq('Y'))
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.orderBy(join.id.asc(),join.updatedDate.desc())
				.fetchResults();
		
		List<JoinDto> content = results.getResults();
		Long total = results.getTotal();
		
		return new PageImpl<>(content,pageable,total);
	}
	
	@Override
	public Page<JoinDto> findPlayerJoinOffer(StatusType statusType, Long playerId, Pageable pageable) {
		QueryResults<JoinDto> results = queryFactory
				.select(Projections.fields(JoinDto.class,join.id,join.player.id.as("playerId"),join.player.playerName,join.team.id.as("teamId"),join.team.teamName,join.requesterType,join.statusType,join.activeYN,join.createdDate,join.updatedDate))
				.from(join)
				.join(join.team,team)
				.where(join.requesterType.eq(RequesterType.TEAM),eqStatusType(statusType),eqPlayerId(playerId),join.activeYN.eq('Y'))
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.orderBy(join.id.asc(),join.updatedDate.desc())
				.fetchResults();
		
		List<JoinDto> content = results.getResults();
		Long total = results.getTotal();
		
		return new PageImpl<>(content,pageable,total);
	}
	
	@Override
	public Page<JoinDto> findTeamJoinApplication(StatusType statusType,Long teamId, Pageable pageable) {
		QueryResults<JoinDto> results = queryFactory
				.select(Projections.fields(JoinDto.class,join.id,join.player.id.as("playerId"),join.player.playerName,join.team.id.as("teamId"),join.team.teamName,join.requesterType,join.statusType,join.activeYN,join.createdDate,join.updatedDate))
				.from(join)
				.join(join.team,team)
				.where(join.requesterType.eq(RequesterType.TEAM),eqStatusType(statusType),eqTeamId(teamId),join.activeYN.eq('Y'))
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.orderBy(join.id.asc(),join.updatedDate.desc())
				.fetchResults();
		
		List<JoinDto> content = results.getResults();
		Long total = results.getTotal();
		
		return new PageImpl<>(content,pageable,total);
	}

	@Override
	public Page<JoinDto> findTeamJoinOffer(StatusType statusType, Long teamId, Pageable pageable) {
		QueryResults<JoinDto> results = queryFactory
				.select(Projections.fields(JoinDto.class,join.id,join.player.id.as("playerId"),join.player.playerName,join.team.id.as("teamId"),join.team.teamName,join.requesterType,join.statusType,join.activeYN,join.createdDate,join.updatedDate))
				.from(join)
				.join(join.player,player)
				.where(join.requesterType.eq(RequesterType.PLAYER),eqStatusType(statusType),eqTeamId(teamId),join.activeYN.eq('Y'))
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.orderBy(join.id.asc(),join.updatedDate.desc())
				.fetchResults();
		
		List<JoinDto> content = results.getResults();
		Long total = results.getTotal();
		
		return new PageImpl<>(content,pageable,total);
	}
	
	private BooleanExpression eqStatusType(StatusType statusType) {
		if(statusType == null) {
			return null;
		}
		return join.statusType.eq(statusType);
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
