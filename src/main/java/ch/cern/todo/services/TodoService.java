package ch.cern.todo.services;

import ch.cern.todo.models.Task;
import ch.cern.todo.models.TaskCategory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;

@Service
public class TodoService {

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, TaskCategory> categories = new HashMap<>();

    public Collection<Task> getTasks(){
        return tasks.values();
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public Task createTask(Task task){
        tasks.put(task.getTaskId(), task);
        return task;
    }

    public Task editTask(Task task){
        if(!tasks.containsKey(task.getTaskId()))
            return null;
        tasks.put(task.getTaskId(), task);
        return task;
    }

    public void deleteTask(int id){
        tasks.remove(id);
    }

    public Collection<TaskCategory> getTaskCategories(){
        return categories.values();
    }


    public TaskCategory getCategory(int id){
        return categories.get(id);
    }

    public TaskCategory createCategory(TaskCategory category){
        categories.put(category.getCategoryId(), category);
        return category;
    }

    public TaskCategory editCategory(TaskCategory category){
        if(!categories.containsKey(category.getCategoryId()))
            return null;
        categories.put(category.getCategoryId(), category);
        return category;
    }

    public void deleteCategory(int id) {
        categories.remove(id);
    }

}
