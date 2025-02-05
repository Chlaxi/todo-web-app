package ch.cern.todo.security;

import ch.cern.todo.models.UserEntity;
import ch.cern.todo.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findFirstByUsername(username);
        if(user != null)
        {
            return new User(
                    user.getUsername(),
                    "",
                    //SecurityConfig.passwordEncoder().encode(user.getPassword()),
                    List.of(new SimpleGrantedAuthority(user.getRoles()))
            );
        } else {
            throw new UsernameNotFoundException("invalid username or password");
        }
    }
}
