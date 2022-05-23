package core.team.service;

import java.util.List;

import core.player.entity.BelongType;
import core.team.dto.TeamDto;

public interface TeamService {
	TeamDto registerTeam(TeamDto team);
	TeamDto searchTeamById(Long id);
	TeamDto searchTeamByName(String teamName);
	List<TeamDto> searchTeams(String teamName,String location,BelongType belongType,String introduction);
	List<TeamDto> searchLocationTeams(String location);
	List<TeamDto> searchBelongTypeTeams(BelongType belongType);
	List<TeamDto> searchAllTeams();
	TeamDto updateTeam(Long id,TeamDto team);
	Long deleteTeam(Long id);
}
