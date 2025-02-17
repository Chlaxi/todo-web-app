package ch.cern.todo.repository;

import ch.cern.todo.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findFirstByUsername(String username);
    Boolean existsByUsername(String username);
}
