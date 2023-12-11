package ru.pominov.taskmanager.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import ru.pominov.taskmanager.dto.comment.CommentDto;
import ru.pominov.taskmanager.dto.comment.NewCommentDto;
import ru.pominov.taskmanager.dto.task.NewTaskDto;
import ru.pominov.taskmanager.dto.task.TaskDto;
import ru.pominov.taskmanager.security.model.UserInfo;
import ru.pominov.taskmanager.security.service.UserInfoService;
import ru.pominov.taskmanager.service.comment.CommentService;
import ru.pominov.taskmanager.service.task.TaskService;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
@SpringBootTest
@RequiredArgsConstructor
@ActiveProfiles("test")
public class CommentServiceIntegrationTests {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private TaskService taskService;

    private UserInfo userInfo;
    private UserInfo userInfo2;
    private NewTaskDto newTaskDto;


    @BeforeEach
    void beforeEach() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        userInfo = new UserInfo(null, "user" + timestamp + "@mail.ru", "Qwerty123", "ROLE_USER");
        userInfo2 = new UserInfo(null, "user2" + timestamp + "@mail.ru", "Qwerty123", "ROLE_USER");
        newTaskDto = new NewTaskDto("Покормить животных", null, "LOW");
    }

    @Test
    void shouldAddNewComment() {
        UserInfo user = userInfoService.addUser(userInfo);
        UserInfo user2 = userInfoService.addUser(userInfo2);
        TaskDto task = taskService.addNewTask(newTaskDto, user.getId());
        assertThat(commentService.addNewComment(task.getId(), new NewCommentDto("Хорошая задача"), user2.getId()))
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("text", "Хорошая задача")
                .hasFieldOrPropertyWithValue("taskId", task.getId())
                .hasFieldOrProperty("author")
                .hasFieldOrProperty("created");
    }


    @Test
    void shouldGetTaskCommentsWithSort() {
        UserInfo user = userInfoService.addUser(userInfo);
        UserInfo user2 = userInfoService.addUser(userInfo2);
        TaskDto task = taskService.addNewTask(newTaskDto, user.getId());
        commentService.addNewComment(task.getId(), new NewCommentDto("Первый комментарий"), user2.getId());
        commentService.addNewComment(task.getId(), new NewCommentDto("Второй комментарий"), user2.getId());

        Page<CommentDto> page = commentService.getTaskComments(task.getId(), "NEWEST_FIRST", 0, 20);

        assertThat(page).isNotNull();
        assertThat(page.getTotalElements()).isEqualTo(2);

        assertThat(page.getContent())
                .hasSize(2)
                .extracting("text")
                .containsExactly("Второй комментарий", "Первый комментарий");
    }
}
