package ch.cern.todo.web;

import ch.cern.todo.models.UserEntity;
import ch.cern.todo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
public class AuthController {
    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("login")
    public String login(){
        return "login";
    }

    @PostMapping("login/create")
    public UserEntity registerUser(@RequestBody @Valid UserEntity user){
        if(userService.findByUsername(user.getUsername()) != null)
            throw new ResponseStatusException(HttpStatus.FOUND);

        user.setRoles(UserEntity.UserRoles.user.name());
        return userService.createUser(user);
    }

    @PostMapping("admin/create")
    public UserEntity registerAdmin(@RequestBody @Valid UserEntity user){
        if(userService.findByUsername(user.getUsername()) != null)
            throw new ResponseStatusException(HttpStatus.FOUND);

        user.setRoles(UserEntity.UserRoles.admin.name());
        return userService.createUser(user);
    }

}
