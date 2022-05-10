package core.team.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import core.player.entity.BelongType;
import core.team.dto.TeamDto;
import core.team.entity.TeamEntity;
import core.team.repository.TeamRepository;

@Service
public class TeamServiceImpl implements TeamService{

	private final TeamRepository teamRepository;
	
	public TeamServiceImpl(TeamRepository teamRepository) {
		this.teamRepository = teamRepository;
	}
	
	@Override
	public TeamDto registerTeam(TeamDto team) {
		TeamEntity teamEntity = teamRepository.save(team.toEntity());
		return teamEntity.toDto();
	}

	@Override
	@Transactional(readOnly=true)
	public TeamDto searchTeamById(Long id) {
		TeamEntity teamEntity = teamRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Id가 존재하지 않습니다."));
		return teamEntity.toDto();
	}
	
	@Override
	@Transactional(readOnly=true)
	public TeamDto searchTeamByName(String teamName) {
		TeamEntity teamEntity = teamRepository.findByTeamName(teamName);
		return teamEntity.toDto();
	}

	@Override
	@Transactional(readOnly=true)
	public List<TeamDto> searchLocationTeams(String location) {
		List<TeamEntity> entityList =  teamRepository.findByLocation(location);
		List<TeamDto> dtoList =  entityList.stream().map(TeamEntity::toDto).collect(Collectors.toList());
		return dtoList;
	}

	@Override
	@Transactional(readOnly=true)
	public List<TeamDto> searchBelongTypeTeams(BelongType belongType) {
		List<TeamEntity> entityList = teamRepository.findByBelongType(belongType);
		List<TeamDto> dtoList =  entityList.stream().map(TeamEntity::toDto).collect(Collectors.toList());
		return dtoList;
	}

	@Override
	@Transactional(readOnly=true)
	public List<TeamDto> searchAllTeams() {
		List<TeamEntity> entityList = teamRepository.findAll();
		List<TeamDto> dtoList = entityList.stream().map(TeamEntity::toDto).collect(Collectors.toList());
		return dtoList;
	}

	@Override
	@Transactional
	public TeamDto updateTeam(Long id,TeamDto team) {
		TeamEntity findTeam = teamRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Id가 존재하지 않습니다."));
		findTeam.setTeamEntity(team);
		return findTeam.toDto();
	}

	@Override
	@Transactional
	public Long deleteTeam(Long id) {
		teamRepository.deleteById(id);
		return id;
	}
	
}
