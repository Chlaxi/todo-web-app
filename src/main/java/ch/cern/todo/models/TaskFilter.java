package ch.cern.todo.models;

import java.sql.Date;
import java.sql.Timestamp;

public class TaskFilter {

    private String taskName;
    private String taskDescription;
    private Timestamp deadlineFrom;
    private Timestamp deadlineTo;
    private int categoryId;

    public Timestamp getDeadlineFrom() {
        return deadlineFrom;
    }

    public void setDeadlineFrom(Timestamp deadlineFrom) {
        this.deadlineFrom = deadlineFrom;
    }

    public Timestamp getDeadlineTo() {
        return deadlineTo;
    }

    public void setDeadlineTo(Timestamp deadlineTo) {
        this.deadlineTo = deadlineTo;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
