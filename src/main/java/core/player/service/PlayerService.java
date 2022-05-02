package core.player.service;

import java.util.List;

import core.player.dto.PlayerDto;
import core.player.entity.PlayerEntity;

public interface PlayerService {
	PlayerDto findPlayer(Long id);
	void createPlayer(PlayerDto player);
	void createPlayers(List<PlayerDto> players);
	void updatePlayer(PlayerDto player);
	void deletePlyaer(Long id);
}
