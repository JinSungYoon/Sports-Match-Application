package core.player.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import core.player.entity.PlayerEntity;

public interface PlayerRepository extends JpaRepository<PlayerEntity,Long> {
	List<PlayerEntity> findByTeam_teamId(Long teamId);
}
