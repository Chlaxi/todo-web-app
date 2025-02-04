package ch.cern.todo.web;

import ch.cern.todo.models.TaskCategory;
import ch.cern.todo.services.TaskCategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/categories")
public class TaskCategoryController {

    private final TaskCategoryService categoryService;

    public TaskCategoryController(TaskCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public Iterable<TaskCategory> getTaskCategories(){
        return categoryService.getTaskCategories();
    }

    @GetMapping("{id}")
    public TaskCategory getCategory(@PathVariable int id){
        var category = categoryService.getCategory(id);
        if(category == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return category;
    }

    @PostMapping("")
    public TaskCategory createCategory(@RequestBody @Valid TaskCategory category){
        //TODO: Parse data into the service
        return categoryService.createCategory(category);
    }

    @PutMapping("{id}/update")
    public TaskCategory editCategory(@PathVariable int id, @RequestBody @Valid TaskCategory category){
        var cat = categoryService.editCategory(id, category);
        if(cat == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return cat;
    }

    @DeleteMapping("{id}")
    public void deleteCategory(@PathVariable int id){
        categoryService.deleteCategory(id);
    }
}
