package core.api.team.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import core.api.player.entity.BelongType;
import core.api.team.entity.TeamEntity;

public interface TeamRepositoryCustom {
	List<TeamEntity> findTeam(String teamName,String location,BelongType belongType,String introduction,Pageable pageable);
}
