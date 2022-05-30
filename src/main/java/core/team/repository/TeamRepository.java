package core.team.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import core.team.entity.TeamEntity;

@Repository
public interface TeamRepository extends JpaRepository<TeamEntity,Long> {
	
}
