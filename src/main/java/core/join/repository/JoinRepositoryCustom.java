package core.join.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import core.join.dto.JoinDto;
import core.join.entity.StatusType;

public interface JoinRepositoryCustom {
	Page<JoinDto> findPlayerJoinRequest(StatusType statusType,Long playerId,Long teamId,Pageable pageable);
	Page<JoinDto> findTeamJoinRequest(StatusType statusType,Long playerId,Long teamId,Pageable pageable);
}
