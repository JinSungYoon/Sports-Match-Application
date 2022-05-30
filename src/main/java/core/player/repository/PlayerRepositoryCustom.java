package core.player.repository;

import java.util.List;

import core.player.entity.PlayerEntity;

public interface PlayerRepositoryCustom {
	List<PlayerEntity> findPlayer(String playerName,Integer uniformNo,String teamName);
}
