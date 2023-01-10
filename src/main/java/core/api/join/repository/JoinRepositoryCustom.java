package core.api.join.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import core.api.join.dto.JoinDto;
import core.api.join.dto.JoinSearchCondition;

public interface JoinRepositoryCustom {
	Page<JoinDto> findPlayerJoinApplication(JoinSearchCondition condition,Long playerId,Pageable pageable);
	Page<JoinDto> findPlayerJoinOffer(JoinSearchCondition condition,Long playerId,Pageable pageable);
	Page<JoinDto> findTeamJoinApplication(JoinSearchCondition condition,Long teamId,Pageable pageable);
	Page<JoinDto> findTeamJoinOffer(JoinSearchCondition condition,Long teamId,Pageable pageable);
	Page<JoinDto> findJoinApplication(JoinSearchCondition condition,Long playerId,Long teamId,Pageable pageable);
	Page<JoinDto> findJoinOffer(JoinSearchCondition condition,Long playerId,Long teamId,Pageable pageable);
}
