package core.player.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import core.player.entity.PlayerEntity;

@Repository
public interface PlayerRepository extends JpaRepository<PlayerEntity,Long> {
	PlayerEntity findByName(String name);
	List<PlayerEntity> findByTeam_teamId(Long teamId);
}
