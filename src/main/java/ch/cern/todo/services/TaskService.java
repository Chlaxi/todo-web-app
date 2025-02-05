package ch.cern.todo.services;

import ch.cern.todo.models.*;
import ch.cern.todo.repository.TaskCategoryRepository;
import ch.cern.todo.repository.TaskRepository;
import ch.cern.todo.repository.UserRepository;
import ch.cern.todo.security.SecurityUtil;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.matching;

@Service
public class TaskService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final TaskCategoryRepository categoryRepository;

    public TaskService(UserRepository userRepository, TaskRepository taskRepository, TaskCategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
    }

    public TaskPagination findTasksByExample(TaskFilter filter, int pageNumber, int pageSize){

        Task task = new Task();
            task.setTaskName(filter.getTaskName());
            task.setTaskDescription(filter.getTaskDescription());
            task.setCategoryId(filter.getCategoryId());

        ExampleMatcher matcher;
        if (task.getCategoryId() == 0) {
            matcher = matching()
                    .withIgnoreCase()
                    .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                    .withIgnoreNullValues()
                    .withIgnorePaths("taskId","categoryId");
        } else {
            matcher = matching()
                    .withIgnoreCase()
                    .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                    .withIgnoreNullValues()
                    .withIgnorePaths("taskId");
        }

        Example<Task> example = Example.of(task, matcher);

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Task> tasks = taskRepository.findAll(example, pageable);


        List<TaskDTO> data;
        if(filter.getDeadlineFrom() != null && filter.getDeadlineTo() != null){
            data = tasks.stream()
                    .filter(t -> t.getDeadline().after(filter.getDeadlineFrom())
                            && t.getDeadline().before(filter.getDeadlineTo()))
                    .map(this::TaskDTOFromTask).toList();
        } else if(filter.getDeadlineFrom() != null && filter.getDeadlineTo() == null)
        {
            data = tasks.stream()
                    .filter(t -> t.getDeadline().after(filter.getDeadlineFrom()))
                    .map(this::TaskDTOFromTask).toList();
        } else if(filter.getDeadlineFrom() == null && filter.getDeadlineTo() != null) {
            data = tasks.stream()
                    .filter(t -> t.getDeadline().before(filter.getDeadlineTo()))
                    .map(this::TaskDTOFromTask).toList();
        }
        else {
            data = tasks.stream().map(this::TaskDTOFromTask).toList();
        }
        Page<TaskDTO> finalPage = new PageImpl<>(data, pageable, data.size());

        return new TaskPagination(
                data,
                pageNumber,
                pageSize,
                finalPage.getTotalElements(),
                finalPage.getTotalPages());
    }

    public TaskPagination getTasks(int pageNumber, int pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Task> tasks = taskRepository.findAll(pageable);
        List<TaskDTO> data = tasks.getContent().stream().map(this::TaskDTOFromTask).toList();

        return new TaskPagination(
                data,
                pageNumber,
                pageSize,
                tasks.getTotalElements(),
                tasks.getTotalPages());
    }

    public TaskDTO getTaskById(int id) {
        Task task =  taskRepository.findById(id).orElse(null);
        return TaskDTOFromTask(task);
    }

    public TaskDTO createTask(Task task){
        String username = SecurityUtil.getSessionUser();
        UserEntity user = userRepository.findFirstByUsername(username);
        task.setOwner(user);
        return TaskDTOFromTask(save(task));
    }

    public TaskDTO editTask(int id, Task newTask){
        String username = SecurityUtil.getSessionUser();
        UserEntity user = userRepository.findFirstByUsername(username);
        newTask.setOwner(user);

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
            setOwner(dto.getOwner());
        }};
    }
}
