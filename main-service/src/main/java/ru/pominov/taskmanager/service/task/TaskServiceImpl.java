package ru.pominov.taskmanager.service.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.pominov.taskmanager.abstraction.PageRequestUtil;
import ru.pominov.taskmanager.dto.task.NewTaskDto;
import ru.pominov.taskmanager.dto.task.TaskDto;
import ru.pominov.taskmanager.dto.task.UpdateTaskDto;
import ru.pominov.taskmanager.exceptions.ConflictException;
import ru.pominov.taskmanager.mapper.TaskMapper;
import ru.pominov.taskmanager.model.Task;
import ru.pominov.taskmanager.model.User;
import ru.pominov.taskmanager.model.enums.TaskPriority;
import ru.pominov.taskmanager.model.enums.TaskSort;
import ru.pominov.taskmanager.model.enums.TaskStatus;
import ru.pominov.taskmanager.repository.TaskRepository;
import ru.pominov.taskmanager.service.user.UserService;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskServiceImpl extends PageRequestUtil implements TaskService {

    private final UserService userService;
    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;

    @Override
    public TaskDto addNewTask(NewTaskDto newTaskDto, Long userId) {
        User author = userService.getExistingUser(userId);
        Task newTask = taskMapper.toTask(newTaskDto);
        newTask.setAuthor(author);
        newTask.setStatus(TaskStatus.WAITING);
        Task savedTask = taskRepository.save(newTask);
        log.info("Add new task for user with ID: {}, task = {}", userId, savedTask);
        return taskMapper.toTaskDto(savedTask);
    }

    @Override
    public TaskDto editTask(Long taskId, UpdateTaskDto updateTaskDto, Long userId) {
        Task existedTask = taskRepository.findTaskByIdAndAuthorId(taskId, userId);
        if (existedTask == null) {
            log.error("User with ID: {} doesn't have task with ID: {}", userId, taskId);
            throw new ConflictException("User with ID: " + userId + " doesn't have task with ID: " + taskId);
        }

        if (updateTaskDto.getTitle() != null) {
            existedTask.setTitle(updateTaskDto.getTitle());
        }
        if (updateTaskDto.getDescription() != null) {
            existedTask.setDescription(updateTaskDto.getDescription());
        }
        if (updateTaskDto.getPriority() != null) {
            existedTask.setPriority(TaskPriority.valueOf(updateTaskDto.getPriority()));
        }
        if (updateTaskDto.getStatus() != null) {
            existedTask.setStatus(TaskStatus.valueOf(updateTaskDto.getStatus()));
        }
        if (updateTaskDto.getPerformerId() != null) {
            User performer = userService.getExistingUser(updateTaskDto.getPerformerId());
            existedTask.setPerformer(performer);
        }

        Task updatedTask = taskRepository.save(existedTask);
        log.info("Update task with ID: {}. New data = {}", taskId, updatedTask);
        return taskMapper.toTaskDto(updatedTask);
    }

    @Override
    public void deleteTask(Long taskId, Long userId) {
        Task existedTask = taskRepository.findTaskByIdAndAuthorId(taskId, userId);
        taskRepository.delete(existedTask);
        log.info("Delete task with ID: {}", taskId);
    }

    @Override
    public Page<TaskDto> getAuthorTasks(Long userId, String sort, Integer from, Integer size) {
        Pageable page = createPageRequest(from, size, TaskSort.valueOf(sort));
        Page<Task> tasks = taskRepository.findAllByAuthorId(userId, page);

        return tasks.map(taskMapper::toTaskDto);
    }

    @Override
    public Page<TaskDto> getPerformerTasks(Long performerId, String sort, Integer from, Integer size) {
        Pageable page = createPageRequest(from, size, TaskSort.valueOf(sort));
        Page<Task> tasks = taskRepository.findAllByPerformerId(performerId, page);

        return tasks.map(taskMapper::toTaskDto);
    }

    @Override
    public TaskDto getAuthorTaskById(Long taskId, Long userId) {
        Task existedTask = taskRepository.findTaskByIdAndAuthorId(taskId, userId);
        if (existedTask == null) {
            log.error("User with ID: {} doesn't have task with ID: {}", userId, taskId);
            throw new ConflictException("User with ID: " + userId + " doesn't have task with ID: " + taskId);
        }
        return taskMapper.toTaskDto(existedTask);
    }

    @Override
    public TaskDto getPerformerTaskById(Long taskId, Long performerId) {
        Task existedTask = taskRepository.findTaskByIdAndPerformerId(taskId, performerId);
        if (existedTask == null) {
            log.error("Performer with ID: {} doesn't execute in task with ID: {}", performerId, taskId);
            throw new ConflictException("Performer with ID: " + performerId + " doesn't execute in task with ID: " + taskId);
        }
        return taskMapper.toTaskDto(existedTask);
    }

    @Override
    public TaskDto changeTaskStatusByPerformer(Long taskId, TaskStatus status, Long performerId) {
        Task existedTask = taskRepository.findTaskByIdAndPerformerId(taskId, performerId);
        if (existedTask == null) {
            log.error("Performer with ID: {} doesn't execute in task with ID: {}", performerId, taskId);
            throw new ConflictException("Performer with ID: " + performerId + " doesn't execute in task with ID: " + taskId);
        }
        existedTask.setStatus(status);
        Task updatedTask = taskRepository.save(existedTask);
        log.info("Performer with ID: {} updated status for task with ID: {} ", performerId, taskId);
        return taskMapper.toTaskDto(updatedTask);
    }
}
