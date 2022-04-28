package core.player.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import core.player.entity.Player;

public interface PlayerRepository extends JpaRepository<Player,String> {

}
