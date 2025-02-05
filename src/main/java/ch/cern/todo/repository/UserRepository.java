package ch.cern.todo.repository;

import ch.cern.todo.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    UserEntity findFirstByUsername(String username);
    //List<UserEntity> findAllByRole(UserEntity.UserRoles role);
}
