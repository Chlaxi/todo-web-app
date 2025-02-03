package ch.cern.todo.models;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.annotation.Id;

public class TaskCategory {

    @Id
    private int categoryId;
    @NotEmpty
    private String categoryName;
    private String categoryDescription;


    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }
}
