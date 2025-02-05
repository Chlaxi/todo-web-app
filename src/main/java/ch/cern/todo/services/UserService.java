package ch.cern.todo.services;

import ch.cern.todo.models.UserEntity;
import ch.cern.todo.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity createUser(UserEntity user){
        return userRepository.save(user);
    }

    public UserEntity findByUsername(String username){
        return userRepository.findFirstByUsername(username);
    }
}
