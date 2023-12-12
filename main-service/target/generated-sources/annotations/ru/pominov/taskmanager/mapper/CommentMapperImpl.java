package ru.pominov.taskmanager.mapper;

import java.time.format.DateTimeFormatter;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.pominov.taskmanager.dto.comment.CommentDto;
import ru.pominov.taskmanager.dto.comment.NewCommentDto;
import ru.pominov.taskmanager.dto.user.UserDto;
import ru.pominov.taskmanager.model.Comment;
import ru.pominov.taskmanager.model.Task;
import ru.pominov.taskmanager.model.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-12-12T21:09:37+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.9 (Amazon.com Inc.)"
)
@Component
public class CommentMapperImpl implements CommentMapper {

    private final DateTimeFormatter dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168 = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" );

    @Override
    public Comment toComment(NewCommentDto newCommentDto) {
        if ( newCommentDto == null ) {
            return null;
        }

        Comment comment = new Comment();

        comment.setText( newCommentDto.getText() );

        return comment;
    }

    @Override
    public CommentDto toCommentDto(Comment comment) {
        if ( comment == null ) {
            return null;
        }

        CommentDto commentDto = new CommentDto();

        commentDto.setTaskId( commentTaskId( comment ) );
        if ( comment.getCreated() != null ) {
            commentDto.setCreated( dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168.format( comment.getCreated() ) );
        }
        commentDto.setId( comment.getId() );
        commentDto.setText( comment.getText() );
        commentDto.setAuthor( userToUserDto( comment.getAuthor() ) );

        return commentDto;
    }

    private Long commentTaskId(Comment comment) {
        if ( comment == null ) {
            return null;
        }
        Task task = comment.getTask();
        if ( task == null ) {
            return null;
        }
        Long id = task.getId();
        if ( id == null ) {
            return null;
        }
        return id;
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
