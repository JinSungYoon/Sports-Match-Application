package core.join.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.querydsl.core.Tuple;

import core.join.entity.StatusType;

public interface JoinRepositoryCustom {
	List<Tuple> findPlayerJoinRequest(StatusType statusType,Long playerId,Pageable pageable);
	List<Tuple> findTeamJoinRequest(StatusType statusType,Long teamId,Pageable pageable);
}
