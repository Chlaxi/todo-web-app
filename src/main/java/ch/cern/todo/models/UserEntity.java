package ch.cern.todo.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Builder
@Data
@Entity
@Table(name = "USERS")
public class UserEntity {

    public enum UserRoles {user,admin}

    public UserEntity() {}

    public UserEntity(int id, String username, String password, String roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    private String roles;
}
