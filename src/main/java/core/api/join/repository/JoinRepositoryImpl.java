package core.api.join.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import core.api.join.dto.JoinDto;
import core.api.join.dto.JoinSearchCondition;
import core.api.join.entity.RequesterType;
import core.api.join.entity.StatusType;
import core.join.entity.QJoinEntity;
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
	public Page<JoinDto> findPlayerJoinApplication(JoinSearchCondition condition,Long playerId,Pageable pageable) {
		QueryResults<JoinDto> results = queryFactory
				.select(Projections.fields(JoinDto.class,join.id,join.player.id.as("playerId"),join.player.playerName,join.team.id.as("teamId"),join.team.teamName,join.requesterType,join.statusType,join.activeYN,join.createdDate,join.updatedDate))
				.from(join)
				.join(join.player,player)
				.where(join.requesterType.eq(condition.getRequesterType()),eqStatusType(condition.getStatusType()),eqPlayerId(playerId),join.activeYN.eq('Y'),betweenDate(condition.getFromDate(),condition.getToDate()))
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.orderBy(join.id.asc(),join.updatedDate.desc())
				.fetchResults();
		
		List<JoinDto> content = results.getResults();
		Long total = results.getTotal();
		
		return new PageImpl<>(content,pageable,total);
	}
	
	@Override
	public Page<JoinDto> findPlayerJoinOffer(JoinSearchCondition condition, Long playerId, Pageable pageable) {
		QueryResults<JoinDto> results = queryFactory
				.select(Projections.fields(JoinDto.class,join.id,join.player.id.as("playerId"),join.player.playerName,join.team.id.as("teamId"),join.team.teamName,join.requesterType,join.statusType,join.activeYN,join.createdDate,join.updatedDate))
				.from(join)
				.join(join.team,team)
				.where(join.requesterType.eq(condition.getRequesterType()),eqStatusType(condition.getStatusType()),eqPlayerId(playerId),join.activeYN.eq('Y'),betweenDate(condition.getFromDate(),condition.getToDate()))
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.orderBy(join.id.asc(),join.updatedDate.desc())
				.fetchResults();
		
		List<JoinDto> content = results.getResults();
		Long total = results.getTotal();
		
		return new PageImpl<>(content,pageable,total);
	}
	
	@Override
	public Page<JoinDto> findTeamJoinApplication(JoinSearchCondition condition,Long teamId, Pageable pageable) {
		QueryResults<JoinDto> results = queryFactory
				.select(Projections.fields(JoinDto.class,join.id,join.player.id.as("playerId"),join.player.playerName,join.team.id.as("teamId"),join.team.teamName,join.requesterType,join.statusType,join.activeYN,join.createdDate,join.updatedDate))
				.from(join)
				.join(join.team,team)
				.where(join.requesterType.eq(condition.getRequesterType()),eqStatusType(condition.getStatusType()),eqTeamId(teamId),join.activeYN.eq('Y'),betweenDate(condition.getFromDate(),condition.getToDate()))
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.orderBy(join.id.asc(),join.updatedDate.desc())
				.fetchResults();
		
		List<JoinDto> content = results.getResults();
		Long total = results.getTotal();
		
		return new PageImpl<>(content,pageable,total);
	}

	@Override
	public Page<JoinDto> findTeamJoinOffer(JoinSearchCondition condition, Long teamId, Pageable pageable) {
		QueryResults<JoinDto> results = queryFactory
				.select(Projections.fields(JoinDto.class,join.id,join.player.id.as("playerId"),join.player.playerName,join.team.id.as("teamId"),join.team.teamName,join.requesterType,join.statusType,join.activeYN,join.createdDate,join.updatedDate))
				.from(join)
				.join(join.player,player)
				.where(join.requesterType.eq(condition.getRequesterType()),eqStatusType(condition.getStatusType()),eqTeamId(teamId),join.activeYN.eq('Y'),betweenDate(condition.getFromDate(),condition.getToDate()))
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.orderBy(join.id.asc(),join.updatedDate.desc())
				.fetchResults();
		
		List<JoinDto> content = results.getResults();
		Long total = results.getTotal();
		
		return new PageImpl<>(content,pageable,total);
	}
	
	@Override
	public Page<JoinDto> findJoinApplication(JoinSearchCondition condition, Long playerId, Long teamId, Pageable pageable) {
		
		List<OrderSpecifier> orders = getAllOrderSpecifiers(pageable);
		
		QueryResults<JoinDto> results = null;
						if(condition.getRequesterType().equals(RequesterType.PLAYER)) {
							results = queryFactory
									.select(Projections.fields(JoinDto.class,join.id,join.player.id.as("playerId"),join.player.playerName,join.team.id.as("teamId"),join.team.teamName,join.requesterType,join.statusType,join.activeYN,join.createdDate,join.updatedDate))
									.from(join)
									.join(join.player,player)
									.where(join.requesterType.eq(condition.getRequesterType()),eqStatusType(condition.getStatusType()),eqPlayerId(playerId),join.activeYN.eq('Y'),betweenDate(condition.getFromDate(),condition.getToDate()))
									.offset(pageable.getOffset())
									.limit(pageable.getPageSize())
									.orderBy(orders.stream().toArray(OrderSpecifier[]::new))
									.fetchResults();
						}else {
							results = queryFactory
									.select(Projections.fields(JoinDto.class,join.id,join.player.id.as("playerId"),join.player.playerName,join.team.id.as("teamId"),join.team.teamName,join.requesterType,join.statusType,join.activeYN,join.createdDate,join.updatedDate))
									.from(join)
									.join(join.team,team)
									.where(join.requesterType.eq(condition.getRequesterType()),eqStatusType(condition.getStatusType()),eqTeamId(teamId),join.activeYN.eq('Y'),betweenDate(condition.getFromDate(),condition.getToDate()))
									.offset(pageable.getOffset())
									.limit(pageable.getPageSize())
									.orderBy(orders.stream().toArray(OrderSpecifier[]::new))
									.fetchResults();
						}
								
		List<JoinDto> content = results.getResults();
		Long total = results.getTotal();
		
		return new PageImpl<>(content,pageable,total);
	}

	@Override
	public Page<JoinDto> findJoinOffer(JoinSearchCondition condition, Long playerId, Long teamId, Pageable pageable) {
		
		List<OrderSpecifier> orders = getAllOrderSpecifiers(pageable);
		
		QueryResults<JoinDto> results = null;
						if(condition.getRequesterType().equals(RequesterType.PLAYER)) {
							results = queryFactory
									.select(Projections.fields(JoinDto.class,join.id,join.player.id.as("playerId"),join.player.playerName,join.team.id.as("teamId"),join.team.teamName,join.requesterType,join.statusType,join.activeYN,join.createdDate,join.updatedDate))
									.from(join)
									.join(join.team,team)
									.where(join.requesterType.eq(condition.getRequesterType()),eqStatusType(condition.getStatusType()),eqTeamId(teamId),join.activeYN.eq('Y'),betweenDate(condition.getFromDate(),condition.getToDate()))
									.offset(pageable.getOffset())
									.limit(pageable.getPageSize())
									.orderBy(orders.stream().toArray(OrderSpecifier[]::new))
									.fetchResults();
						}else {
							results = queryFactory
									.select(Projections.fields(JoinDto.class,join.id,join.player.id.as("playerId"),join.player.playerName,join.team.id.as("teamId"),join.team.teamName,join.requesterType,join.statusType,join.activeYN,join.createdDate,join.updatedDate))
									.from(join)
									.join(join.player,player)
									.where(join.requesterType.eq(condition.getRequesterType()),eqStatusType(condition.getStatusType()),eqPlayerId(playerId),join.activeYN.eq('Y'),betweenDate(condition.getFromDate(),condition.getToDate()))
									.offset(pageable.getOffset())
									.limit(pageable.getPageSize())
									.orderBy(orders.stream().toArray(OrderSpecifier[]::new))
									.fetchResults();
						}		
				
		List<JoinDto> content = results.getResults();
		Long total = results.getTotal();
		
		return new PageImpl<>(content,pageable,total);
	}
	
	// 동적 정렬을 위한 단일 OrderSpecifier 추출 method
//	private OrderSpecifier getOrderSpecifiers(Pageable pageable) {
//		// 순회하며 정렬조건대로 OrderSpecifier 반환
//		if(!pageable.getSort().isEmpty()) {
//			for(Sort.Order order : pageable.getSort()) {
//				Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
//				switch(order.getProperty()) {
//					case "statusType":
//						return new OrderSpecifier(direction,join.statusType);
//					case "requesterType":
//						return new OrderSpecifier(direction,join.requesterType);
//					case "playerName":
//						return new OrderSpecifier(direction,join.player.playerName);
//					case "teamName":
//						return new OrderSpecifier(direction,join.team.teamName);
//					case "updatedDate":
//						return new OrderSpecifier(direction,join.updatedDate);
//					default:
//						return new OrderSpecifier(direction,join.id); 
//				}
//			}
//		}
//		
//		return new OrderSpecifier(Order.ASC,join.id);
//	}
	
	private OrderSpecifier getOrderSpecifiers(Pageable pageable) {
		// 순회하며 정렬조건대로 OrderSpecifier 반환
		if(!pageable.getSort().isEmpty()) {
			for(Sort.Order order : pageable.getSort()) {
				Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
				String prop = order.getProperty(); 
				PathBuilder orderPath = new PathBuilder(join.getClass(),"joinEntity");
				new OrderSpecifier(direction,orderPath.get(prop));
			}
		}
		
		return new OrderSpecifier(Order.ASC,join.id);
	}
	
	// 동적 정렬을 위한 다중 OrderSpecifier 추출 method
	private List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable){
		List<OrderSpecifier> orders = new ArrayList<>();
		
		if(!pageable.getSort().isEmpty()) {
			for(Sort.Order order : pageable.getSort()) {
				Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
				String prop = order.getProperty(); 
				PathBuilder orderPath = new PathBuilder(join.getClass(),"joinEntity");
				orders.add(new OrderSpecifier(direction,orderPath.get(prop)));
			}
		}
		
		return orders;
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
	
	private BooleanExpression betweenDate(LocalDateTime fromDate,LocalDateTime toDate) {
		
		if(fromDate == null) {
			return null;
		}else if(toDate == null) {
			return null;
		}
		
		return join.updatedDate.between(fromDate, toDate);
		
	}
	
}
