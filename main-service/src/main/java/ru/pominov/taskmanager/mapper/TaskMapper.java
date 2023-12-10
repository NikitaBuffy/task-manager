package ru.pominov.taskmanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.pominov.taskmanager.dto.task.NewTaskDto;
import ru.pominov.taskmanager.dto.task.TaskDto;
import ru.pominov.taskmanager.model.Task;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    Task toTask(NewTaskDto newTaskDto);

    TaskDto toTaskDto(Task task);
}
