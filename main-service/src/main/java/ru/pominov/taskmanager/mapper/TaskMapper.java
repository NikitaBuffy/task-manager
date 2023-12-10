package ru.pominov.taskmanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.pominov.taskmanager.dto.NewTaskDto;
import ru.pominov.taskmanager.dto.TaskDto;
import ru.pominov.taskmanager.model.Task;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    Task toTask(NewTaskDto newTaskDto);

    TaskDto toTaskDto(Task task);
}
