package ch.cern.todo.web;

import ch.cern.todo.models.Task;
import ch.cern.todo.models.TaskCategory;
import ch.cern.todo.services.TodoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

@RestController
public class TodoController {

    private final TodoService service;

    public TodoController(TodoService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String Test() {
        return "Test";
    }

    @GetMapping("/tasks")
    public Collection<Task> getTasks(){
        return service.getTasks();
    }

    @GetMapping("/categories")
    public Collection<TaskCategory> getTaskCategories(){
        return service.getTaskCategories();
    }

    @GetMapping("/tasks/{id}")
    public Task getTask(@PathVariable int id){
        var task = service.getTask(id);
        if(task == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return task;
    }

    @PostMapping("/tasks")
    public Task createTask(@RequestBody @Valid Task task){
        return service.createTask(task);
    }

    @PutMapping("/tasks/{id}")
    public Task editTask(@RequestBody @Valid Task task){
        var t = service.editTask(task);
        if(t == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return t;
    }

    @DeleteMapping("/tasks/{id}")
    public void deleteTask(@PathVariable int id){
        service.deleteTask(id);
    }

    @GetMapping("/categories/{id}")
    public TaskCategory getCategory(@PathVariable int id){
        var category = service.getCategory(id);
        if(category == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return category;
    }

    @PostMapping("/categories")
    public TaskCategory createCategory(@RequestBody @Valid TaskCategory category){
        //TODO: Parse data into the service
        return service.createCategory(category);
    }

    @PutMapping("/categories/{id}")
    public TaskCategory editCategory(@RequestBody @Valid TaskCategory category){
        var cat = service.editCategory(category);
        if(cat == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return cat;
    }

    @DeleteMapping("/categories/{id}")
    public void deleteCategory(@PathVariable int id){
        service.deleteCategory(id);
    }

}
