package core.api.join.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import core.api.join.entity.JoinEntity;

public interface JoinRepository extends JpaRepository<JoinEntity,Long> {
	
}
