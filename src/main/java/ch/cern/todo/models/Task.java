package ch.cern.todo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;

@Entity
@Table(name = "TASKS")
public class Task implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int taskId;
    @NotEmpty
    private String taskName;
    private String taskDescription;
    @NotNull
    private Timestamp deadline;
    @NotNull
    private int categoryId;
    private int ownerId;

    public Task() {

    }
    public Task(String taskName) {
        this.taskName = taskName;
        deadline = new Timestamp(Calendar.getInstance().get(Calendar.MILLISECOND));
        this.categoryId = 1;
    }
    public Task(String taskName, String taskDescription) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        deadline = new Timestamp(Calendar.getInstance().get(Calendar.MILLISECOND));
        this.categoryId = 1;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
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

    public Timestamp getDeadline() {
        return deadline;
    }

    public void setDeadline(Timestamp deadline) {
        this.deadline = deadline;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getOwner() {
        return ownerId;
    }

    public void setOwner(UserEntity owner) {
        this.ownerId = owner.getId();
    }
}
