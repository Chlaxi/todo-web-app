package ch.cern.todo.web;

import ch.cern.todo.models.UserEntity;
import ch.cern.todo.models.UserRegisterDto;
import ch.cern.todo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;

    @Autowired
    public AuthController(UserService userService, PasswordEncoder passwordEncoder, AuthenticationManager authManager) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserRegisterDto dto){
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(),dto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(auth);

        return new ResponseEntity<>("Signed in successfully", HttpStatus.OK);
    }

    @PostMapping("/login/create")
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserRegisterDto userDto){
        if(userService.existsByUsername(userDto.getUsername())) {
            return new ResponseEntity<>("Username is already taken", HttpStatus.BAD_REQUEST);
        }
        UserEntity user = UserEntity.builder()
                    .username(userDto.getUsername())
                    .password(passwordEncoder.encode(userDto.getPassword()))
                    .roles(UserEntity.UserRoles.USER.name())
                .build();
        userService.createUser(user);
        return new ResponseEntity<>("Success!", HttpStatus.OK);
    }

    @PostMapping("/admin/create")
    public ResponseEntity<String> registerAdmin(@RequestBody @Valid UserRegisterDto userDto){
        if(userService.existsByUsername(userDto.getUsername())) {
            return new ResponseEntity<>("Username is already taken", HttpStatus.BAD_REQUEST);
        }
        UserEntity user = UserEntity.builder()
                    .username(userDto.getUsername())
                    .password(passwordEncoder.encode(userDto.getPassword()))
                    .roles(UserEntity.UserRoles.ADMIN.name())
                .build();


        userService.createUser(user);
        return new ResponseEntity<>("Success!", HttpStatus.OK);
    }

}
