package ch.cern.todo.web;

import ch.cern.todo.models.Task;
import ch.cern.todo.models.TaskDTO;
import ch.cern.todo.services.TaskCategoryService;
import ch.cern.todo.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskCategoryService categoryService;

    public TaskController(TaskService taskService, TaskCategoryService categoryService) {
        this.taskService = taskService;
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public Iterable<TaskDTO> getTasks(){
        return taskService.getTasks();
    }

    @GetMapping("{id}")
    public TaskDTO getTask(@PathVariable int id){
        var task = taskService.getTaskById(id);
        if(task == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return task;
    }

    @PostMapping("")
    public TaskDTO createTask(@RequestBody @Valid Task task){
        return taskService.createTask(task);
    }

    @PutMapping("{id}/update")
    public TaskDTO editTask(@PathVariable int id, @RequestBody @Valid Task task){
        var t = taskService.editTask(id, task);
        if(t == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return t;
    }

    @DeleteMapping("{id}")
    public void deleteTask(@PathVariable int id){
        taskService.deleteTask(id);
    }
}
