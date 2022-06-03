package core.join.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import core.join.entity.JoinEntity;

public interface JoinRepository extends JpaRepository<JoinEntity,Long> {

}
