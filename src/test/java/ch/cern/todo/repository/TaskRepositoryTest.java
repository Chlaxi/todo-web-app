package ch.cern.todo.repository;

import ch.cern.todo.models.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        taskRepository.deleteAll();
    }

    @Test
    public void TestTaskSave(){
        Task task = new Task("Test");
        
        var response = taskRepository.save(task);
        Assertions.assertEquals(1, taskRepository.count());
        Assertions.assertEquals(
                task.getTaskName(),
                response.getTaskName());
    }

    @Test
    public void TestTaskSaveMultiple(){
        Task task1 = new Task("Test");
        var response1 = taskRepository.save(task1);
        Assertions.assertEquals(
                task1.getTaskName(),
                response1.getTaskName());
        Assertions.assertEquals(1, taskRepository.count());

        Task task2 = new Task("Test 2");
        var response2 = taskRepository.save(task2);
        Assertions.assertEquals(
                task2.getTaskName(),
                response2.getTaskName());
        Assertions.assertEquals(2, taskRepository.count());
    }

    @Test
    public void TestTaskFindById() {
        var task = taskRepository.save(new Task("Test"));
        Assertions.assertEquals(1, taskRepository.count(),
                "Failed creating task during setup");

        var result = taskRepository.findById(task.getTaskId());

        Assertions.assertNotNull(result);
    }

    @Test
    public void TestTaskFindAllCategories() {
        for (int i = 0; i < 10; i++) {
            taskRepository.save(new Task("Test"+i));
        }

        var result = taskRepository.findAll();

        Assertions.assertEquals(10, result.size());
    }

    @Test
    public void TestTaskDelete() {
        var task = taskRepository.save(new Task("Test"));
        Assertions.assertEquals(1, taskRepository.count(),
                "Failed creating task during setup");

        taskRepository.delete(task);
        Assertions.assertEquals(0, taskRepository.count(),
                "task was not deleted");
    }

    @Test
    public void TestTaskUpdateTask(){
        var setupCat = taskRepository.save(new Task("Test"));
        Assertions.assertEquals(1, taskRepository.count(),
                "Failed creating task during setup");

        var task = taskRepository.getById(setupCat.getTaskId());
        task.setTaskName("Updated name");
        task.setTaskDescription("Lorem ipsum");

        var result = taskRepository.save(task);
        Assertions.assertEquals(1, taskRepository.count(),
                "A new entry was created instead of updating");
        Assertions.assertEquals(task.getTaskId(),
                result.getTaskId());
        Assertions.assertEquals(task.getTaskName(),
                result.getTaskName());
        Assertions.assertEquals(task.getTaskDescription(),
                result.getTaskDescription());

    }
}
