package ch.cern.todo.services;

import ch.cern.todo.models.TaskCategory;
import ch.cern.todo.repository.TaskCategoryRepository;
import ch.cern.todo.repository.TaskRepository;
import ch.cern.todo.web.TodoController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TodoController.class)
class TodoServiceTest {

    private TaskCategoryRepository categoryRepository;
    private TaskRepository taskRepository;

    @Test
    void getTasks() {

    }

    @Test
    void getTask() {
    }
/*
    @Test
    void testCreateTask() {
        TaskCategory category = new TaskCategory();
        category.setCategoryName("Default");
        category.setCategoryDescription("Test description");

        var response = service.createCategory(category);
        Assertions.assertEquals(response.getCategoryName(),
                category.getCategoryName(),
                "Name was not created correctly");
        Assertions.assertEquals(response.getCategoryDescription(),
                category.getCategoryDescription(),
                "Description was not created correctly");
        Assertions.assertEquals(1, response.getCategoryId(), "ID was not set");

    }*/

    @Test
    void editTask() {
    }

    @Test
    void deleteTask() {
    }

    @Test
    void getTaskCategories() {
    }

    @Test
    void getCategory() {
    }

    @Test
    void createCategory() {
    }

    @Test
    void editCategory() {
    }

    @Test
    void deleteCategory() {
    }
}