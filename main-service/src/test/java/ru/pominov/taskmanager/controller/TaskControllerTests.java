package ru.pominov.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.pominov.taskmanager.dto.task.NewTaskDto;
import ru.pominov.taskmanager.dto.task.TaskDto;
import ru.pominov.taskmanager.dto.user.UserDto;
import ru.pominov.taskmanager.model.enums.TaskPriority;
import ru.pominov.taskmanager.model.enums.TaskStatus;
import ru.pominov.taskmanager.security.model.UserInfo;
import ru.pominov.taskmanager.security.service.UserInfoService;
import ru.pominov.taskmanager.service.task.TaskService;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@RequiredArgsConstructor
public class TaskControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    TaskService taskService;

    @MockBean
    UserInfoService userInfoService;

    NewTaskDto newTaskDto = new NewTaskDto("Покормить кота", null, "LOW");
    NewTaskDto newTaskDtoWithEmptyTitle = new NewTaskDto("", null, "LOW");
    TaskDto taskDtoResponse = new TaskDto(1L, "Покормить кота", null, TaskStatus.WAITING, TaskPriority.LOW, new UserDto(1L, "Никита", "Поминов"), null);
    TaskDto taskDtoResponseWithPerformer = new TaskDto(1L, "Покормить кота", null, TaskStatus.IN_PROGRESS, TaskPriority.LOW, new UserDto(1L, "Никита", "Поминов"), new UserDto(2L, "Алексей", "Алексеев"));
    UserInfo userInfo = new UserInfo(1L, "email@email.ru", "Qwerty123", "ROLE_USER");


    @Test
    @WithMockUser(username = "email@email.ru", authorities = "ROLE_USER")
    void shouldThrowBadRequestWhenNewTaskTitleIsBlank() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/author/tasks/new")
                        .content(mapper.writeValueAsString(newTaskDtoWithEmptyTitle))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "email@email.ru", authorities = "ROLE_USER")
    void shouldAddNewTask() throws Exception {
        when(taskService.addNewTask(newTaskDto, 1L)).thenReturn(taskDtoResponse);
        when(userInfoService.getUserInfo("email@email.ru")).thenReturn(userInfo);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/author/tasks/new")
                        .principal(new TestingAuthenticationToken("email@email.ru", "Qwerty123", "ROLE_USER"))
                        .content(mapper.writeValueAsString(newTaskDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Покормить кота"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("WAITING"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.priority").value("LOW"));
    }

    @Test
    @WithMockUser(username = "email@email.ru", authorities = "ROLE_USER")
    void shouldDeleteTask() throws Exception {
        when(userInfoService.getUserInfo("email@email.ru")).thenReturn(userInfo);
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/author/tasks/1")
                        .principal(new TestingAuthenticationToken("email@email.ru", "Qwerty123", "ROLE_USER")))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "email@email.ru", authorities = "ROLE_USER")
    void shouldGetAuthorTasks() throws Exception {
        when(userInfoService.getUserInfo("email@email.ru")).thenReturn(userInfo);

        Page<TaskDto> taskPage = new PageImpl<>(Collections.singletonList(taskDtoResponse),
                PageRequest.of(0, 20), 1);
        when(taskService.getAuthorTasks(1L, "ASC_ID", 0, 20)).thenReturn(taskPage);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/author/tasks")
                        .principal(new TestingAuthenticationToken("email@email.ru", "Qwerty123",
                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))))
                        .param("sort", "ASC_ID")
                        .param("from", "0")
                        .param("size", "20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].title").value("Покормить кота"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].status").value("WAITING"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].priority").value("LOW"));
    }

    @Test
    @WithMockUser(username = "email@email.ru", authorities = "ROLE_USER")
    void shouldThrowBadRequestWhileGerAuthorTasksWhenSortParameterIsNotSupported() throws Exception {
        when(userInfoService.getUserInfo("email@email.ru")).thenReturn(userInfo);
        when(taskService.getAuthorTasks(1L, "HELLO", 0, 20)).thenThrow(IllegalArgumentException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/author/tasks")
                        .principal(new TestingAuthenticationToken("email@email.ru", "Qwerty123",
                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))))
                        .param("sort", "HELLO")
                        .param("from", "0")
                        .param("size", "20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "email@email.ru", authorities = "ROLE_USER")
    void shouldChangeTaskStatusByPerformer() throws Exception {
        when(userInfoService.getUserInfo("email@email.ru")).thenReturn(userInfo);
        when(taskService.changeTaskStatusByPerformer(1L, TaskStatus.IN_PROGRESS, 1L)).thenReturn(taskDtoResponseWithPerformer);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/performer/tasks/1")
                        .principal(new TestingAuthenticationToken("email@email.ru", "Qwerty123", "ROLE_USER"))
                        .param("status", "IN_PROGRESS")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("IN_PROGRESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.performer.id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.performer.firstName").value("Алексей"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.performer.lastName").value("Алексеев"));
    }
}
