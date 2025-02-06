package ch.cern.todo.services;

import ch.cern.todo.models.*;
import ch.cern.todo.repository.TaskCategoryRepository;
import ch.cern.todo.repository.TaskRepository;
import ch.cern.todo.repository.UserRepository;
import ch.cern.todo.security.SecurityUtil;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

        String username = SecurityUtil.getSessionUser();
        UserEntity user = userRepository.findFirstByUsername(username).orElseThrow(() -> new UsernameNotFoundException("invalid username"));

        Task task = new Task();
            task.setTaskName(filter.getTaskName());
            task.setTaskDescription(filter.getTaskDescription());
            task.setCategory(categoryRepository.findById(filter.getCategoryId()).orElse(null));

        if (filter.getOwner() == null) {
            task.setOwner(user);
        } else {
            if(!user.getRoles().equals(UserEntity.UserRoles.ADMIN.name()) &&
                user.getId() != filter.getOwner().getId()){
                return null; //User is not authorised to view the tasks
            }
            UserEntity filterUser = new UserEntity();
            filterUser.setId(filter.getOwner().getId());
            task.setOwner(filterUser);
        }

        ExampleMatcher matcher;
        if (task.getCategory() == null) {
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
                    .map(this::TaskToDto).toList();
        } else if(filter.getDeadlineFrom() != null)
        {
            data = tasks.stream()
                    .filter(t -> t.getDeadline().after(filter.getDeadlineFrom()))
                    .map(this::TaskToDto).toList();
        } else if(filter.getDeadlineTo() != null) {
            data = tasks.stream()
                    .filter(t -> t.getDeadline().before(filter.getDeadlineTo()))
                    .map(this::TaskToDto).toList();
        }
        else {
            data = tasks.stream().map(this::TaskToDto).toList();
        }
        Page<TaskDTO> finalPage = new PageImpl<>(data, pageable, data.size());

        return new TaskPagination(
                data,
                pageNumber,
                pageSize,
                finalPage.getTotalElements(),
                finalPage.getTotalPages());
    }

    public TaskPagination getAllTasks(int pageNumber, int pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Task> tasks = taskRepository.findAll(pageable);
        List<TaskDTO> data = tasks.getContent().stream()
                .map(this::TaskToDto)
                .toList();

        return new TaskPagination(
                data,
                pageNumber,
                pageSize,
                tasks.getTotalElements(),
                tasks.getTotalPages());
    }

    public TaskDTO getTaskById(int id) {
        Task task =  taskRepository.findById(id).orElse(null);
        return TaskToDto(task);
    }

    public TaskDTO createTask(TaskDTO task){
        if(!categoryRepository.existsById(task.getCategory().getCategoryId()))
            return null;

        String username = SecurityUtil.getSessionUser();
        UserEntity user = userRepository.findFirstByUsername(username).orElseThrow(() -> new UsernameNotFoundException("invalid username or password"));
        task.setOwner(new UserDto(user));

        Task t = DTOtoTask(task, user);

        Task newTask = taskRepository.save(t);
        return TaskToDto(newTask);
    }

    public TaskDTO editTask(int id, TaskDTO newTask){
        Task task = taskRepository.findById(id).orElse(null);
        if(task == null)
            return null;

        String username = SecurityUtil.getSessionUser();
        UserEntity user = userRepository.findFirstByUsername(username).orElseThrow(() -> new UsernameNotFoundException("invalid username or password"));
        if(user.getId() != task.getOwner().getId())
            return null;

        TaskCategory category = categoryRepository.findById(newTask.getCategory().getCategoryId()).orElse(null);
        if(category == null)
            throw new IndexOutOfBoundsException();

        task.setTaskId(newTask.getTaskId());
        task.setTaskName(newTask.getTaskName());
        task.setTaskDescription(newTask.getTaskDescription());
        task.setDeadline(newTask.getDeadline());
        task.setCategory(category);
        task.setOwner(user);

        Task updatedTask = taskRepository.save(task);

        return TaskToDto(updatedTask, user);
    }

    public boolean deleteTask(int id){
        if(!taskRepository.existsById(id))
            return false;

        taskRepository.deleteById(id);
        return true;
    }

    private TaskDTO TaskToDto(Task task){
        UserEntity user =  Optional.of(userRepository.findById(task.getOwner().getId()).orElseThrow()).get();
        return TaskToDto(task, user);
    }

    private TaskDTO TaskToDto(Task task, UserEntity user){
        if(task == null)
            return null;

        if(!categoryRepository.existsById(task.getCategory().getCategoryId()))
            return null;

        return TaskDTO.builder()
                .taskId(task.getTaskId())
                .taskName(task.getTaskName())
                .taskDescription(task.getTaskDescription())
                .deadline(task.getDeadline())
                .category(task.getCategory())
                .owner(new UserDto(user))
                .build();
    }

    private Task DTOtoTask(TaskDTO dto, UserEntity user){
        Task task = new Task();
            task.setTaskId(dto.getTaskId());
            task.setTaskName(dto.getTaskName());
            task.setTaskDescription(dto.getTaskDescription());
            task.setDeadline(dto.getDeadline());
            task.setCategory(dto.getCategory());
            task.setOwner(user);

        return task;
    }
}
