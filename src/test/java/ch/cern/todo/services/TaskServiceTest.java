package ch.cern.todo.services;

import ch.cern.todo.models.Task;
import ch.cern.todo.models.TaskCategory;
import ch.cern.todo.repository.TaskCategoryRepository;
import ch.cern.todo.repository.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock private TaskCategoryRepository categoryRepository;
    @Mock private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;
    @InjectMocks private TaskCategoryService categoryService;

    @Test
    void getTasks() {

    }

    @Test
    void getTaskById() {
        Task task = new Task("Test");

        when(taskRepository.findById(1)).thenReturn(Optional.ofNullable(task));
        when(categoryRepository.findById(1)).thenReturn(Optional.of(new TaskCategory("Test")));
        var response = taskService.getTaskById(1);

        Assertions.assertNotNull(response);
    }

    @Test
    void testCreateTask() {
        Task task = new Task("Test");

        when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);
        var response = taskService.createTask(task);

        Assertions.assertEquals(response.getTaskName(),
                task.getTaskName(),
                "Name was not created correctly");
        Assertions.assertEquals(response.getTaskDescription(),
                task.getTaskDescription(),
                "Description was not created correctly");
    }

    @Test
    void editTask() {
        when(taskRepository.existsById(1)).thenReturn(true);
        when(categoryRepository.existsById(1)).thenReturn(true);
        Task task = new Task("Updated", "Updated description");
        task.setTaskId(1);
        when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);
        var response = taskService.editTask(1, task);

        Assertions.assertEquals("Updated", response.getTaskName(),
                "Name was not created correctly");
        Assertions.assertEquals("Updated description",
                response.getTaskDescription(),
                "Description was not created correctly");
    }

    @Test
    void deleteTaskSuccessEntryExists() {
        when(taskRepository.existsById(1)).thenReturn(true);
        Assertions.assertTrue(taskService.deleteTask(1));
    }

    @Test
    void deleteTaskEntryNotExist() {
        when(taskRepository.existsById(1)).thenReturn(false);
        Assertions.assertFalse(taskService.deleteTask(1));
    }

    /* TODO: Reimplement with pagination
    @Test
    void getTaskCategories() {
        Page<TaskCategory> mock = Mockito.mock(Page.class);
        when(categoryRepository.findAll(Mockito.any(Pageable.class))).thenReturn(mock);
        var response = service.getTaskCategories();

        Assertions.assertNotNull(response);
    }*/

    @Test
    void getCategory() {
        TaskCategory category = new TaskCategory("Default", "Test description");

        when(categoryRepository.findById(1)).thenReturn(Optional.ofNullable(category));
        var response = categoryService.getCategory(1);

        Assertions.assertNotNull(response);
    }

    @Test
    void createCategory() {
        TaskCategory category = new TaskCategory("Default", "Test description");

        when(categoryRepository.save(Mockito.any(TaskCategory.class))).thenReturn(category);
        var response = categoryService.createCategory(category);

        Assertions.assertEquals(response.getCategoryName(),
                category.getCategoryName(),
                "Name was not created correctly");
        Assertions.assertEquals(response.getCategoryDescription(),
                category.getCategoryDescription(),
                "Description was not created correctly");
    }

    @Test
    void TestCategoryEditTitleAndDescription() {
        when(categoryRepository.existsById(1)).thenReturn(true);
        TaskCategory category = new TaskCategory("Updated",
                "Updated description");
        category.setCategoryId(1);
        var response = categoryService.editCategory(1, category);

        Assertions.assertEquals("Updated", response.getCategoryName(),
                "Name was not created correctly");
        Assertions.assertEquals("Updated description",
                response.getCategoryDescription(),
                "Description was not created correctly");
    }

    @Test
    void deleteCategorySuccessEntryExists() {
        when(categoryRepository.existsById(1)).thenReturn(true);
        Assertions.assertTrue(categoryService.deleteCategory(1));
    }

    @Test
    void deleteCategoryEntryNotExist() {
        when(categoryRepository.existsById(1)).thenReturn(false);
        Assertions.assertFalse(categoryService.deleteCategory(1));
    }
}