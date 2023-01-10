package core.api.team.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import core.api.player.entity.BelongType;
import core.api.team.dto.TeamDto;
import core.api.team.entity.TeamEntity;
import core.api.team.repository.TeamRepository;
import core.api.team.repository.TeamRepositoryImpl;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService{

	private final TeamRepository teamRepository;
	
	private final TeamRepositoryImpl teamRepositoryImpl;
	
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
	public List<TeamDto> searchAllTeams(Pageable pageable) {
		Page<TeamEntity> entityList = teamRepository.findAll(pageable);
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

	@Override
	public List<TeamDto> searchTeams(String teamName,String location,BelongType belongType,String introduction,Pageable pageable) {
		List<TeamEntity> list = teamRepositoryImpl.findTeam(teamName, location, belongType, introduction,pageable);
		return list.stream().map(item->item.toDto()).collect(Collectors.toList());
	}
	
}
