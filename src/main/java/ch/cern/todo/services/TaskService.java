package ch.cern.todo.services;

import ch.cern.todo.models.Task;
import ch.cern.todo.models.TaskCategory;
import ch.cern.todo.models.TaskDTO;
import ch.cern.todo.models.TaskPagination;
import ch.cern.todo.repository.TaskCategoryRepository;
import ch.cern.todo.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public TaskPagination getTasks(int pageNumber, int pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Task> tasks = taskRepository.findAll(pageable);
        List<TaskDTO> data = tasks.getContent().stream().map(this::TaskDTOFromTask).toList();

        TaskPagination page = new TaskPagination(
                data,
                pageNumber,
                pageSize,
                tasks.getTotalElements(),
                tasks.getTotalPages());

        return page;
    }

    public TaskDTO getTaskById(int id) {
        Task task =  taskRepository.findById(id).orElse(null);
        return TaskDTOFromTask(task);
    }

    public TaskDTO createTask(Task task){
        return TaskDTOFromTask(save(task));
    }

    public TaskDTO editTask(int id, Task newTask){
        Task task = taskRepository.findById(id).orElse(null);
        if(task == null)
            return null;

        Task updatedTask = save(newTask);
        return TaskDTOFromTask(updatedTask);
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
