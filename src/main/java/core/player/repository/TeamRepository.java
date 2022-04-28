package core.player.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import core.player.entity.Team;

public interface TeamRepository extends JpaRepository<Team,String> {

}
