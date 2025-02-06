package ch.cern.todo.models;

import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

@Data
public class TaskFilter {

    private String taskName;
    private String taskDescription;
    private Timestamp deadlineFrom;
    private Timestamp deadlineTo;
    private int categoryId;
    private UserDto owner;
}
