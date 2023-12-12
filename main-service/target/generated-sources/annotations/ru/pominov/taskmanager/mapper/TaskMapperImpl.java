package ru.pominov.taskmanager.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.pominov.taskmanager.dto.task.NewTaskDto;
import ru.pominov.taskmanager.dto.task.TaskDto;
import ru.pominov.taskmanager.dto.user.UserDto;
import ru.pominov.taskmanager.model.Task;
import ru.pominov.taskmanager.model.User;
import ru.pominov.taskmanager.model.enums.TaskPriority;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-12-12T21:09:37+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.9 (Amazon.com Inc.)"
)
@Component
public class TaskMapperImpl implements TaskMapper {

    @Override
    public Task toTask(NewTaskDto newTaskDto) {
        if ( newTaskDto == null ) {
            return null;
        }

        Task task = new Task();

        task.setTitle( newTaskDto.getTitle() );
        task.setDescription( newTaskDto.getDescription() );
        if ( newTaskDto.getPriority() != null ) {
            task.setPriority( Enum.valueOf( TaskPriority.class, newTaskDto.getPriority() ) );
        }

        return task;
    }

    @Override
    public TaskDto toTaskDto(Task task) {
        if ( task == null ) {
            return null;
        }

        TaskDto taskDto = new TaskDto();

        taskDto.setId( task.getId() );
        taskDto.setTitle( task.getTitle() );
        taskDto.setDescription( task.getDescription() );
        taskDto.setStatus( task.getStatus() );
        taskDto.setPriority( task.getPriority() );
        taskDto.setAuthor( userToUserDto( task.getAuthor() ) );
        taskDto.setPerformer( userToUserDto( task.getPerformer() ) );

        return taskDto;
    }

    protected UserDto userToUserDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setId( user.getId() );
        userDto.setFirstName( user.getFirstName() );
        userDto.setLastName( user.getLastName() );

        return userDto;
    }
}
