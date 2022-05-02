package core.player.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import core.player.dto.PlayerDto;
import core.player.entity.PlayerEntity;
import core.player.entity.TeamEntity;
import core.player.repository.PlayerRepository;
import core.player.repository.TeamRepository;

@Service
public class PlayerServiceImpl implements PlayerService {

	
	private final PlayerRepository playerRepository;
	private final TeamRepository teamRepository;
	
	public PlayerServiceImpl(final PlayerRepository playerRepository,final TeamRepository teamRepository) {
		this.playerRepository = playerRepository;
		this.teamRepository   = teamRepository;
	}
	
	
	@Override
	public PlayerDto findPlayer(Long id) {
		PlayerEntity playerEntity = playerRepository.findById(id).orElse(new PlayerEntity());
		return playerEntity.toDto();
	}

	@Override
	public void createPlayer(PlayerDto player) {
		try {
			
			// 만일 존재하는 Player ID라면 저장하지 않음.
			if(player.getTeam()!=null) {
				String teamName = player.getName();
				
				TeamEntity team = teamRepository.findByName(teamName);
				
				List<PlayerEntity> playerList =  playerRepository.findByTeam_teamId(team.getTeamId());
				
				List<Integer> uniformNoList =  playerList.stream().map(PlayerEntity::getUniformNo).collect(Collectors.toList());
				// 같은 팀에 동일한 유니폼 번호를 가질 수 없으므로 검사 필요.
				if(uniformNoList.contains(player.getUniformNo())) {
					throw new Exception("Uniform 번호는 중복 될 수 없습니다.");
				}
			}else {
				playerRepository.save(player.toEntity());
			}
		}catch (Exception e) {
			e.getStackTrace();
		}
		
	}
	

	@Override
	public void createPlayers(List<PlayerDto> players) {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public void deletePlyaer(Long id) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void updatePlayer(PlayerDto player) {
		// TODO Auto-generated method stub
		
	}

}