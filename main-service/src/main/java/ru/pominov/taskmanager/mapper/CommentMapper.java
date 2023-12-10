package ru.pominov.taskmanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.pominov.taskmanager.dto.comment.CommentDto;
import ru.pominov.taskmanager.dto.comment.NewCommentDto;
import ru.pominov.taskmanager.model.Comment;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    Comment toComment(NewCommentDto newCommentDto);

    @Mapping(source = "task.id", target = "taskId")
    @Mapping(source = "created", target = "created", dateFormat = "yyyy-MM-dd HH:mm:ss")
    CommentDto toCommentDto(Comment comment);
}
