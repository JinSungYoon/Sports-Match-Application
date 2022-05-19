package core.team.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import core.player.entity.BelongType;
import core.team.entity.TeamEntity;

@Repository
public interface TeamRepository extends JpaRepository<TeamEntity,Long> {
	
	TeamEntity findByTeamName(String name);
	@Query(value = "SELECT t FROM Team t WHERE t.teamName LIKE %:teamName% AND t.location LIKE %:location% AND t.belongType :belongType AND t.introducetion LIKE %:introducetion%")
	List<TeamEntity> findByTeamNameContainingAndLocationContainingAndBelongTypeAndIntroductionContaining(@Param("teamName")String teamName,@Param("location")String location,@Param("belongType")BelongType belongType ,@Param("introduction")String introduction);
	List<TeamEntity> findByLocation(String location);
	List<TeamEntity> findByBelongType(BelongType belongType);
	List<TeamEntity> findByIntroductionLike(String keyword);
	
	
}
