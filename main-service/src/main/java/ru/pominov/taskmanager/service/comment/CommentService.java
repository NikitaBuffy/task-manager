package ru.pominov.taskmanager.service.comment;

import org.springframework.data.domain.Page;
import ru.pominov.taskmanager.dto.comment.CommentDto;
import ru.pominov.taskmanager.dto.comment.NewCommentDto;

public interface CommentService {

    CommentDto addNewComment(Long taskId, NewCommentDto newCommentDto, Long userId);

    Page<CommentDto> getTaskComments(Long taskId, String sort, Integer from, Integer size);
}
