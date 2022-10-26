package core.join.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import core.join.dto.JoinDto;
import core.join.dto.JoinSearchCondition;

public interface JoinRepositoryCustom {
	Page<JoinDto> findPlayerJoinApplication(JoinSearchCondition condition,Long playerId,Pageable pageable);
	Page<JoinDto> findPlayerJoinOffer(JoinSearchCondition condition,Long playerId,Pageable pageable);
	Page<JoinDto> findTeamJoinApplication(JoinSearchCondition condition,Long teamId,Pageable pageable);
	Page<JoinDto> findTeamJoinOffer(JoinSearchCondition condition,Long teamId,Pageable pageable);
}
