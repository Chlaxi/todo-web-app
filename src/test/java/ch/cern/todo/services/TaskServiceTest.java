package ch.cern.todo.services;

import ch.cern.todo.models.*;
import ch.cern.todo.repository.TaskCategoryRepository;
import ch.cern.todo.repository.TaskRepository;
import ch.cern.todo.repository.UserRepository;
import ch.cern.todo.security.SecurityConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.sql.Timestamp;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(loader= AnnotationConfigContextLoader.class, classes = SecurityConfig.class)
class TaskServiceTest {

    @Mock private TaskCategoryRepository categoryRepository;
    @Mock private TaskRepository taskRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;
    @InjectMocks private TaskCategoryService categoryService;

    private TaskCategory category;
    private UserEntity user;
    private TaskDTO taskDto;
    private Task task;

    @BeforeEach
    public void init(){
        category = new TaskCategory("test", "test description");
        category.setCategoryId(1);

        user = UserEntity.builder()
                .id(1)
                .username("admin")
                .password("admin")
                .roles(UserEntity.UserRoles.admin.name()).build();

        taskDto = TaskDTO.builder()
                .taskId(1)
                .taskName("Task")
                .taskDescription("Description")
                .deadline(Timestamp.valueOf("2025-02-07 12:00:00"))
                .category(category)
                .owner(new UserDto(user))
                .build();

        task = new Task();
            task.setTaskId(1);
            task.setTaskName("Task");
            task.setTaskDescription("Description");
            task.setDeadline(Timestamp.valueOf("2025-02-07 12:00:00"));
            task.setCategory(category);
            task.setOwner(user);
    }

    @Test
    void getTasks() {
        Page<Task> page = Mockito.mock(Page.class);
        when(taskRepository.findAll(Mockito.any(Pageable.class))).thenReturn(page);
        TaskPagination response = taskService.getTasks(0,10);

        Assertions.assertNotNull(response);
    }

    @Test
    void getTaskById() {
        Task task = new Task("Test");
        task.setTaskId(1);
        task.setCategory(category);
        task.setOwner(Mockito.mock(UserEntity.class));

        when(taskRepository.findById(1)).thenReturn(Optional.of(task));
        when(categoryRepository.existsById(category.getCategoryId())).thenReturn(true);
        var response = taskService.getTaskById(1);

        Assertions.assertNotNull(response);
    }

    @Test
    void testCreateTask() {
        when(categoryRepository.existsById(category.getCategoryId())).thenReturn(true);
        when(userRepository.findFirstByUsername("")).thenReturn(mock(UserEntity.class));
       // when(categoryRepository.existsById(Mockito.any(Integer.class))).thenReturn(true);
        when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);
        var response = taskService.createTask(taskDto);

        Assertions.assertEquals(response.getTaskName(),
                taskDto.getTaskName(),
                "Name was not created correctly");
        Assertions.assertEquals(response.getTaskDescription(),
                taskDto.getTaskDescription(),
                "Description was not created correctly");
    }

    @Test
    void editTask() {
        TaskSaveDTO oldTask = new TaskSaveDTO();
        oldTask.setTaskId(1);

        Task task = new Task("Updated", "Updated description");
        task.setTaskId(1);

        when(taskRepository.findById(1)).thenReturn(Optional.of(task));
        when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);
        when(categoryRepository.existsById(1)).thenReturn(true);
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        var response = taskService.editTask(1, oldTask);

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