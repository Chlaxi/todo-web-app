package ch.cern.todo.models;

public class UserDto {
    private int id;
    private String username;
    private String roles;

    public UserDto(UserEntity user){
        id = user.getId();
        username = user.getUsername();
        roles = user.getRoles();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}
