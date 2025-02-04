package ch.cern.todo.services;

import ch.cern.todo.models.Task;
import ch.cern.todo.models.TaskCategory;
import ch.cern.todo.models.TaskDTO;
import ch.cern.todo.repository.TaskCategoryRepository;
import ch.cern.todo.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskCategoryRepository categoryRepository;

    public TaskService(TaskRepository taskRepository, TaskCategoryRepository categoryRepository) {
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
    }

    public Iterable<TaskDTO> getTasks(){
        List<Task> task = taskRepository.findAll();
        return task.stream().map(this::TaskDTOFromTask).toList();
    }

    public TaskDTO getTaskById(int id) {
        Task task =  taskRepository.findById(id).orElse(null);
        return TaskDTOFromTask(task);
    }

    public Task createTask(Task task){
        return save(task);
    }

    public Task editTask(int id, Task task){
        if(!taskRepository.existsById(id))
            return null;

        if(task.getTaskId() != id)
            return null;

        return save(task);
    }

    public boolean deleteTask(int id){
        if(!taskRepository.existsById(id))
            return false;

        taskRepository.deleteById(id);
        return true;
    }

    private Task save(Task task){
        if(!categoryRepository.existsById(task.getCategoryId()))
            task.setCategoryId(1);

        return taskRepository.save(task);
    }

    private TaskDTO TaskDTOFromTask(Task task){
        if(task == null)
            return null;

        TaskCategory category = categoryRepository.findById(task.getCategoryId()).orElse(null);
        if(category == null)
            return null;

        return new TaskDTO() {{
            setTaskId(task.getTaskId());
            setTaskName(task.getTaskName());
            setTaskDescription(task.getTaskDescription());
            setDeadline(task.getDeadline());
            setCategory(category);
        }};
    }

    private Task DTOtoTask(TaskDTO dto){
        return new Task() {{
            setTaskId(dto.getTaskId());
            setTaskName(dto.getTaskName());
            setTaskDescription(dto.getTaskDescription());
            setDeadline(dto.getDeadline());
            setCategoryId(dto.getCategory().getCategoryId());
        }};
    }
}
