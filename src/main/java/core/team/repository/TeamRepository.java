package core.team.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import core.player.entity.BelongType;
import core.team.entity.TeamEntity;

@Repository
public interface TeamRepository extends JpaRepository<TeamEntity,Long> {
	
	TeamEntity findByTeamName(String name);
	List<TeamEntity> findByLocation(String location);
	List<TeamEntity> findByBelongType(BelongType belongType);
	List<TeamEntity> findByIntroductionLike(String keyword);
	
	
}
