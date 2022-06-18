package core.join.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import core.join.dto.JoinDto;
import core.join.entity.StatusType;

public interface JoinRepositoryCustom {
	List<JoinDto> findPlayerJoinRequest(StatusType statusType,Long playerId,Pageable pageable);
	List<JoinDto> findTeamJoinRequest(StatusType statusType,Long teamId,Pageable pageable);
}
