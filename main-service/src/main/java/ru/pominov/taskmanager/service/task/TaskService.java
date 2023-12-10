package ru.pominov.taskmanager.service.task;

import org.springframework.data.domain.Page;
import ru.pominov.taskmanager.dto.task.NewTaskDto;
import ru.pominov.taskmanager.dto.task.TaskDto;
import ru.pominov.taskmanager.dto.task.UpdateTaskDto;
import ru.pominov.taskmanager.model.enums.TaskStatus;

public interface TaskService {

    TaskDto addNewTask(NewTaskDto newTaskDto, Long userId);

    TaskDto editTask(Long taskId, UpdateTaskDto updateTaskDto, Long userId);

    void deleteTask(Long taskId, Long userId);

    Page<TaskDto> getAuthorTasks(Long userId, String sort, Integer from, Integer size);

    Page<TaskDto> getPerformerTasks(Long performerId, String sort, Integer from, Integer size);

    TaskDto getAuthorTaskById(Long taskId, Long userId);

    TaskDto getPerformerTaskById(Long taskId, Long performerId);

    TaskDto changeTaskStatusByPerformer(Long taskId, TaskStatus status, Long performerId);
}
