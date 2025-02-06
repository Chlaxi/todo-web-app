package ch.cern.todo.web;

import ch.cern.todo.models.*;
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

    @GetMapping("/all")
    public TaskPagination getTasks(@RequestBody(required = false) Task task,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {

            return taskService.getAllTasks(pageNumber, pageSize);
    }

    @PostMapping("/Search")
    public TaskPagination findTasksByExample(@RequestBody(required = false) TaskFilter task,
                                   @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                   @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {

            return taskService.findTasksByExample(task, pageNumber, pageSize);
    }

    @GetMapping("{id}")
    public TaskDTO getTask(@PathVariable int id){
        var task = taskService.getTaskById(id);
        if(task == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return task;
    }

    @PostMapping("")
    public TaskDTO createTask(@RequestBody @Valid TaskDTO task){
        return taskService.createTask(task);
    }

    @PutMapping("{id}/update")
    public TaskDTO editTask(@PathVariable int id, @RequestBody @Valid TaskDTO task){
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
