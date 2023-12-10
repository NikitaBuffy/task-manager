package ru.pominov.taskmanager.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.pominov.taskmanager.abstraction.PageRequestUtil;
import ru.pominov.taskmanager.dto.comment.CommentDto;
import ru.pominov.taskmanager.dto.comment.NewCommentDto;
import ru.pominov.taskmanager.mapper.CommentMapper;
import ru.pominov.taskmanager.model.Comment;
import ru.pominov.taskmanager.model.Task;
import ru.pominov.taskmanager.model.User;
import ru.pominov.taskmanager.model.enums.CommentSort;
import ru.pominov.taskmanager.repository.CommentRepository;
import ru.pominov.taskmanager.service.task.TaskService;
import ru.pominov.taskmanager.service.user.UserService;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl extends PageRequestUtil implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final TaskService taskService;
    private final UserService userService;

    @Override
    public CommentDto addNewComment(Long taskId, NewCommentDto newCommentDto, Long userId) {
        Task task = taskService.getExistingTask(taskId);
        User user = userService.getExistingUser(userId);
        Comment comment = commentMapper.toComment(newCommentDto);
        comment.setAuthor(user);
        comment.setTask(task);
        comment.setCreated(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);
        log.info("Saved new comment to task with ID: {}. Comment data = {}", taskId, savedComment);
        return commentMapper.toCommentDto(savedComment);
    }

    @Override
    public Page<CommentDto> getTaskComments(Long taskId, String sort, Integer from, Integer size) {
        taskService.getExistingTask(taskId);
        Pageable page = createPageRequest(from, size, CommentSort.valueOf(sort));
        Page<Comment> comments = commentRepository.findAllByTaskId(taskId, page);

        return comments.map(commentMapper::toCommentDto);
    }
}
