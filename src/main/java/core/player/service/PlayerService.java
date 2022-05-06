package core.player.service;

import java.util.List;

import core.player.dto.PlayerDto;
import core.player.entity.PlayerEntity;

public interface PlayerService {
	PlayerDto findOnePlayer(Long id);
	List<PlayerDto> findPlayerAll();
	PlayerDto registerPlayer(PlayerDto player);
	List<PlayerDto> registerPlayers(List<PlayerDto> players);
	PlayerDto updatePlayer(Long id,PlayerDto player);
	Long deletePlyaer(Long id);
}
