package ru.pominov.taskmanager.service;

import org.springframework.data.domain.Page;
import ru.pominov.taskmanager.dto.NewTaskDto;
import ru.pominov.taskmanager.dto.TaskDto;
import ru.pominov.taskmanager.dto.UpdateTaskDto;
import ru.pominov.taskmanager.model.enums.TaskStatus;

public interface TaskService {

    TaskDto addNewTask(NewTaskDto newTaskDto, Long userId);

    TaskDto editTask(Long taskId, UpdateTaskDto updateTaskDto, Long userId);

    void deleteTask(Long taskId, Long userId);

    Page<TaskDto> getAuthorTasks(Long userId, String sort, Integer from, Integer size);

    TaskDto getAuthorTaskById(Long taskId, Long userId);

    TaskDto changeTaskStatusByPerformer(Long taskId, TaskStatus status, Long performerId);
}
