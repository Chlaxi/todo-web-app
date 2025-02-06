package ch.cern.todo.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
public class TaskDTO {



    private int taskId;
    private String taskName;
    private String taskDescription;
    private Timestamp deadline;
    private TaskCategory category;
    private UserDto owner;
}
