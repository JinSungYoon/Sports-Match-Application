package core.player.service;

import java.util.List;

import core.player.dto.PlayerDto;
import core.player.entity.PlayerEntity;

public interface PlayerService {
	PlayerDto searchOnePlayer(Long id);
	List<PlayerDto> searchPlayerAll();
	PlayerDto registerPlayer(PlayerDto player);
	List<PlayerDto> registerPlayers(List<PlayerDto> players);
	PlayerDto updatePlayer(Long id,PlayerDto player);
	Long deletePlyaer(Long id);
}
