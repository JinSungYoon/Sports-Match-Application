package core.join.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import core.join.dto.JoinDto;
import core.join.entity.StatusType;

public interface JoinRepositoryCustom {
	Page<JoinDto> findPlayerJoinApplication(StatusType statusType,Long playerId,Long teamId,Pageable pageable);
	Page<JoinDto> findPlayerJoinOffer(StatusType statusType,Long playerId,Long teamId,Pageable pageable);
	Page<JoinDto> findTeamJoinApplication(StatusType statusType,Long playerId,Long teamId,Pageable pageable);
	Page<JoinDto> findTeamJoinOffer(StatusType statusType,Long playerId,Long teamId,Pageable pageable);
}
