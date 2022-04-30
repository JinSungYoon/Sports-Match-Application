package core.player.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import core.player.entity.TeamEntity;

public interface TeamRepository extends JpaRepository<TeamEntity,Long> {
	TeamEntity findByName(String name);
}
