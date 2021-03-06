package core.team.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import core.player.entity.BelongType;
import core.team.entity.TeamEntity;

public interface TeamRepositoryCustom {
	List<TeamEntity> findTeam(String teamName,String location,BelongType belongType,String introduction,Pageable pageable);
}
