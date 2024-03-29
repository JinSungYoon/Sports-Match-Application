package core.api.team.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import core.api.player.entity.BelongType;
import core.api.team.dto.TeamDto;

public interface TeamService {
	TeamDto registerTeam(TeamDto team);
	TeamDto searchTeamById(Long id);
	List<TeamDto> searchTeams(String teamName,String location,BelongType belongType,String introduction,Pageable pageable);
	List<TeamDto> searchAllTeams(Pageable pageable);
	TeamDto updateTeam(Long id,TeamDto team);
	Long deleteTeam(Long id);
}
