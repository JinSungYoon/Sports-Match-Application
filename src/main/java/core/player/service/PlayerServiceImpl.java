package core.player.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import core.player.dto.PlayerDto;
import core.player.entity.PlayerEntity;
import core.player.repository.PlayerRepository;
import core.team.entity.TeamEntity;
import core.team.repository.TeamRepository;

@Service
public class PlayerServiceImpl implements PlayerService {
	
	private final PlayerRepository playerRepository;
	private final TeamRepository teamRepository;
	
	public PlayerServiceImpl(PlayerRepository playerRepository,TeamRepository teamRepository) {
		this.playerRepository = playerRepository;
		this.teamRepository   = teamRepository;
	}
	
	
	@Override
	@Transactional(readOnly=true)	// JPA 변경감지 내부 기능 비활성화, update시 정합성을 유지 
	public PlayerDto searchOnePlayer(Long id) {
		PlayerEntity playerEntity = playerRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Id가 존재하지 않습니다."));
		return playerEntity.toDto();
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<PlayerDto> searchPlayerAll() {
		List<PlayerEntity> entityList = playerRepository.findAll();
		List<PlayerDto> dtoList = entityList.stream().map(PlayerEntity::toDto).collect(Collectors.toList());
		return dtoList;
	}

	
	@Override
	@Transactional
	public PlayerDto registerPlayer(PlayerDto player) {
		// 만일 존재하는 Player ID라면 저장하지 않음.
		if(player.getTeam()!=null) {
			// player가 속한 team에 팀원을 조회하여, uniform번호가 중복되는지 확인.
			List<PlayerEntity> playerList =  playerRepository.findByTeam_teamName(player.getTeam().getTeamName());
			// 만일 팀이 존재하지 않는다면, 팀을 등록하고 선수를 등록해준다.
			if(playerList.size() == 0) {
				//teamRepository.save(player.getTeam().toEntity());
				PlayerEntity playerEntity = playerRepository.save(player.toEntity());
				return playerEntity.toDto();
			}else {
				List<Integer> uniformNoList =  playerList.stream().map(PlayerEntity::getUniformNo).collect(Collectors.toList());
				// 같은 팀에 동일한 유니폼 번호를 가질 수 없으므로 검사 필요.
				if(uniformNoList.contains(player.getUniformNo())) {
					//throw new Exception("Uniform 번호는 중복 될 수 없습니다.");
					throw new IllegalArgumentException("Uniform 번호는 중복 될 수 없습니다.");
				}else {
					PlayerEntity playerEntity = playerRepository.save(player.toEntity());
					return playerEntity.toDto();
				}
			}
		}else {
			PlayerEntity playerEntity = playerRepository.save(player.toEntity());
			return playerEntity.toDto();
		}
	}
	

	@Override
	@Transactional
	public List<PlayerDto> registerPlayers(List<PlayerDto> players) {
		List<PlayerDto> list= new ArrayList<PlayerDto>();
		return list;
		
	}
	
	@Override
	@Transactional
	public PlayerDto updatePlayer(Long id,PlayerDto player) {
		PlayerEntity originPlayer = playerRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Id가 존재하지 않습니다."));
		originPlayer.setPlayerEntity(player);
		return originPlayer.toDto();
	}
	
	
	@Override
	@Transactional
	public Long deletePlyaer(Long id) {
		playerRepository.deleteById(id);
		return id;
	}


	

}
