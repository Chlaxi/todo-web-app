package ch.cern.todo.services;

import ch.cern.todo.models.*;
import ch.cern.todo.repository.TaskCategoryRepository;
import ch.cern.todo.repository.TaskRepository;
import ch.cern.todo.repository.UserRepository;
import ch.cern.todo.security.SecurityConfig;

import ch.cern.todo.security.SecurityUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
@WithMockUser(username = "admin", roles = "admin")
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
    public void setUp(){
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
        Page<Task> page = new PageImpl<>(List.of(task));
        when(taskRepository.findAll(Mockito.any(Pageable.class))).thenReturn(page);
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(categoryRepository.existsById(category.getCategoryId())).thenReturn(true);
        TaskPagination response = taskService.getTasks(0,10);

        Assertions.assertNotNull(response);
    }

    @Test
    void getTaskById() {
        when(taskRepository.findById(task.getTaskId())).thenReturn(Optional.of(task));
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(categoryRepository.existsById(category.getCategoryId())).thenReturn(true);
        var response = taskService.getTaskById(task.getTaskId());

        Assertions.assertNotNull(response);
    }

    @Test
    void testCreateTask() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(categoryRepository.existsById(category.getCategoryId())).thenReturn(true);
        when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(user.getUsername());
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findFirstByUsername(user.getUsername())).thenReturn(user);

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
        TaskDTO newTaskDto = TaskDTO.builder()
                        .taskId(task.getTaskId())
                        .taskName("Updated")
                        .taskDescription("Updated description")
                        .deadline(task.getDeadline())
                        .category(category)
                        .owner(new UserDto(task.getOwner()))
                        .build();


        when(taskRepository.findById(1)).thenReturn(Optional.of(task));

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(user.getUsername());
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findFirstByUsername(user.getUsername())).thenReturn(user);

        when(categoryRepository.findById(category.getCategoryId())).thenReturn(Optional.of(category));
        when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);
        when(categoryRepository.existsById(category.getCategoryId())).thenReturn(true);

        var response = taskService.editTask(1, newTaskDto);

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