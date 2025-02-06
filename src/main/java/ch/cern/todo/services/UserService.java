package ch.cern.todo.services;

import ch.cern.todo.models.UserEntity;
import ch.cern.todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }

    public UserEntity createUser(UserEntity user){
        return userRepository.save(user);
    }

    public UserEntity findByUsername(String username){
        return userRepository.findFirstByUsername(username).orElse(null);
    }
}
