package ch.cern.todo.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class UserDto {
    private int id;
    private String username;
    private String roles;

    public UserDto(){}

    public UserDto(UserEntity user){
        id = user.getId();
        username = user.getUsername();
        roles = user.getRoles();
    }
}
