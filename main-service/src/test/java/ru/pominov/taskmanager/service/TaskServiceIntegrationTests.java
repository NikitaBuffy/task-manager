package ru.pominov.taskmanager.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.pominov.taskmanager.dto.task.NewTaskDto;
import ru.pominov.taskmanager.dto.task.TaskDto;
import ru.pominov.taskmanager.dto.task.UpdateTaskDto;
import ru.pominov.taskmanager.exceptions.ConflictException;
import ru.pominov.taskmanager.exceptions.DataNotFoundException;
import ru.pominov.taskmanager.model.enums.TaskPriority;
import ru.pominov.taskmanager.model.enums.TaskStatus;
import ru.pominov.taskmanager.security.model.UserInfo;
import ru.pominov.taskmanager.security.service.UserInfoService;
import ru.pominov.taskmanager.service.task.TaskService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@AutoConfigureMockMvc
@SpringBootTest
@RequiredArgsConstructor
@ActiveProfiles("test")
public class TaskServiceIntegrationTests {

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

    @Transactional
    @Test
    void addNewTaskShouldAdd() {
        userInfoService.addUser(userInfo);
        assertThat(taskService.addNewTask(newTaskDto, 1L))
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("title", "Покормить животных")
                .hasFieldOrProperty("description")
                .hasFieldOrPropertyWithValue("status", TaskStatus.WAITING)
                .hasFieldOrPropertyWithValue("priority", TaskPriority.LOW)
                .hasFieldOrProperty("author")
                .hasFieldOrProperty("performer");
    }

    @Test
    void addNewTaskShouldThrowDataNotFoundExceptionWhenUserDoesntExist() {
        assertThrows(DataNotFoundException.class, () ->  taskService.addNewTask(newTaskDto, 100L));
    }

    @Test
    void editTaskShouldThrowConflictExceptionWhenUserIsNotAuthor() {
        UserInfo user1 = userInfoService.addUser(userInfo);
        UserInfo user2 = userInfoService.addUser(userInfo2);
        taskService.addNewTask(newTaskDto, user1.getId());
        assertThrows(ConflictException.class, () ->  taskService.editTask(1L, new UpdateTaskDto(), user2.getId()));
    }

    @Test
    void editTaskShouldEditPriorityAndSetPerformerAndEditDescription() {
        UserInfo user1 = userInfoService.addUser(userInfo);
        UserInfo user2 = userInfoService.addUser(userInfo2);
        TaskDto task = taskService.addNewTask(newTaskDto, user1.getId());
        assertThat(taskService.editTask(task.getId(), new UpdateTaskDto(null, "В 8 утра сухими кормом", "HIGH", null, user2.getId()), user1.getId()))
                .hasFieldOrPropertyWithValue("priority", TaskPriority.HIGH)
                .hasFieldOrProperty("performer").hasNoNullFieldsOrProperties();
    }

    @Test
    void shouldDeleteTask() {
        UserInfo user = userInfoService.addUser(userInfo);
        TaskDto task = taskService.addNewTask(newTaskDto, user.getId());
        taskService.deleteTask(task.getId(), user.getId());
        assertThrows(DataNotFoundException.class, () -> taskService.getExistingTask(task.getId()));
    }

    @Test
    void shouldGetPerformerTasks() {
        UserInfo user = userInfoService.addUser(userInfo);
        UserInfo user2 = userInfoService.addUser(userInfo2);
        TaskDto task = taskService.addNewTask(newTaskDto, user.getId());
        taskService.editTask(task.getId(), new UpdateTaskDto(null, null, null, null, user2.getId()), user.getId());

        Page<TaskDto> page = taskService.getPerformerTasks(user2.getId(), "DESC_ID", 0, 20);

        assertThat(page).isNotNull();
        assertThat(page.getTotalElements()).isEqualTo(1);

        assertThat(page.getContent())
                .hasSize(1)
                .extracting("title")
                .containsExactly("Покормить животных");
    }

    @Test
    void getAuthorTaskByIdShouldThrowConflictException() {
        UserInfo user = userInfoService.addUser(userInfo);
        TaskDto task = taskService.addNewTask(newTaskDto, user.getId());

        assertThrows(ConflictException.class, () -> taskService.getAuthorTaskById(task.getId(), 100L));
    }

    @Test
    void getAuthorTaskByIdShouldReturnAuthorTask() {
        UserInfo user = userInfoService.addUser(userInfo);
        TaskDto task = taskService.addNewTask(newTaskDto, user.getId());

        assertThat(taskService.getAuthorTaskById(task.getId(), user.getId()))
                .hasFieldOrPropertyWithValue("id", task.getId());
    }

    @Test
    void getPerformerTaskByIdShouldThrowConflictException() {
        UserInfo user = userInfoService.addUser(userInfo);
        UserInfo user2 = userInfoService.addUser(userInfo2);
        TaskDto task = taskService.addNewTask(newTaskDto, user.getId());
        taskService.editTask(task.getId(), new UpdateTaskDto(null, null, null, null, user2.getId()), user.getId());

        assertThrows(ConflictException.class, () -> taskService.getPerformerTaskById(100L, user2.getId()));
    }

    @Test
    void changeTaskStatusByPerformerShouldChangeStatus() {
        UserInfo user = userInfoService.addUser(userInfo);
        UserInfo user2 = userInfoService.addUser(userInfo2);
        TaskDto task = taskService.addNewTask(newTaskDto, user.getId());
        taskService.editTask(task.getId(), new UpdateTaskDto(null, null, null, null, user2.getId()), user.getId());

        assertThat(taskService.changeTaskStatusByPerformer(task.getId(), TaskStatus.DONE, user2.getId()))
                .hasFieldOrPropertyWithValue("id", task.getId())
                .hasFieldOrPropertyWithValue("status", TaskStatus.DONE)
                .hasFieldOrPropertyWithValue("performer.id", user2.getId());
    }

    @Test
    void changeTaskStatusByPerformerShouldThrowConflictException() {
        UserInfo user = userInfoService.addUser(userInfo);
        UserInfo user2 = userInfoService.addUser(userInfo2);
        TaskDto task = taskService.addNewTask(newTaskDto, user.getId());

        assertThrows(ConflictException.class, () -> taskService.changeTaskStatusByPerformer(task.getId(), TaskStatus.DONE, user2.getId()));
    }
}
