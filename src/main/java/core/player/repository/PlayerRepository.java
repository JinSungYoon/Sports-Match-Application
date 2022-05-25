package core.player.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import core.player.entity.PlayerEntity;

@Repository
public interface PlayerRepository extends JpaRepository<PlayerEntity,Long> {
	PlayerEntity findByPlayerName(String name);
	@Query(value = "SELECT p FROM Player p where p.playerName LIKE :playerName order by p.uniformNo desc;")  
	List<PlayerEntity> findByPlayerNameContaining(@Param("playerName")String playerName);
	List<PlayerEntity> findByTeam_id(Long teamId);
	@Query(value = "SELECT p FROM Player p JOIN INNER JOIN Team t ON p.id = t.id WHERE t.teamName = :teamName")
	List<PlayerEntity> findByTeam_teamName(@Param("teamName")String teamName);
	
}
