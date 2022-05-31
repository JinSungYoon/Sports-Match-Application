package core.team.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import core.team.entity.TeamEntity;

@Repository
public interface TeamRepository extends JpaRepository<TeamEntity,Long> {
	Page<TeamEntity> findAll(Pageable pageable);
}
