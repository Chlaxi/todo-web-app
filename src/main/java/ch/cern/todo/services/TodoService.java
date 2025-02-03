package ch.cern.todo.services;

import ch.cern.todo.models.Task;
import ch.cern.todo.models.TaskCategory;
import ch.cern.todo.repository.TaskCategoryRepository;
import ch.cern.todo.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TodoService {

    private final TaskRepository taskRepository;
    private final TaskCategoryRepository categoryRepository;

    public TodoService(TaskRepository taskRepository, TaskCategoryRepository categoryRepository) {
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
    }

    public Iterable<Task> getTasks(){
        return taskRepository.findAll();
    }

    public Task getTask(int id) {
        return taskRepository.findById(id).orElse(null);
    }

    public Task createTask(Task task){
        taskRepository.save(task);
        return task;
    }

    public Task editTask(Task task){
        if(taskRepository.findById(task.getTaskId()).isEmpty())
            return null;

        taskRepository.save(task);
        return task;
    }

    public void deleteTask(int id){
        taskRepository.deleteById(id);
    }

    public Iterable<TaskCategory> getTaskCategories(){
        return categoryRepository.findAll();
    }


    public TaskCategory getCategory(int id){
        return categoryRepository.findById(id).orElse(null);
    }

    public TaskCategory createCategory(TaskCategory category){
        categoryRepository.save(category);
        return category;
    }

    public TaskCategory editCategory(TaskCategory category){
        if(!categoryRepository.existsById(category.getCategoryId()))
            return null;

        categoryRepository.save(category);
        return category;
    }

    public void deleteCategory(int id) {
        categoryRepository.deleteById(id);
    }

}
