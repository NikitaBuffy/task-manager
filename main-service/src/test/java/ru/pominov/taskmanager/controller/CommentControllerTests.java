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
import ru.pominov.taskmanager.dto.comment.CommentDto;
import ru.pominov.taskmanager.dto.comment.NewCommentDto;
import ru.pominov.taskmanager.dto.user.UserDto;
import ru.pominov.taskmanager.security.model.UserInfo;
import ru.pominov.taskmanager.security.service.UserInfoService;
import ru.pominov.taskmanager.service.comment.CommentService;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@RequiredArgsConstructor
public class CommentControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    CommentService commentService;

    @MockBean
    UserInfoService userInfoService;

    NewCommentDto newCommentDto = new NewCommentDto("Интересная задача");
    NewCommentDto newCommentDtoWithEmptyText = new NewCommentDto("");
    CommentDto commentDtoResponse = new CommentDto(1L, "Интересная задача", 1L, new UserDto(2L, "Дмитрий", "Лодочкин"), LocalDateTime.now().toString());
    UserInfo userInfo = new UserInfo(1L, "email@email.ru", "Qwerty123", "ROLE_USER");


    @Test
    @WithMockUser(username = "email@email.ru", authorities = "ROLE_USER")
    void shouldThrowBadRequestWhenNewCommentTextIsBlank() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/tasks/1/comment")
                        .content(mapper.writeValueAsString(newCommentDtoWithEmptyText))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "email@email.ru", authorities = "ROLE_USER")
    void shouldAddNewComment() throws Exception {
        when(userInfoService.getUserInfo("email@email.ru")).thenReturn(userInfo);
        when(commentService.addNewComment(1L, newCommentDto, 1L)).thenReturn(commentDtoResponse);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/tasks/1/comment")
                        .content(mapper.writeValueAsString(newCommentDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value("Интересная задача"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.taskId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author.id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author.firstName").value("Дмитрий"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author.lastName").value("Лодочкин"));
    }

    @Test
    @WithMockUser(username = "email@email.ru", authorities = "ROLE_USER")
    void shouldGetTaskComments() throws Exception {
        when(userInfoService.getUserInfo("email@email.ru")).thenReturn(userInfo);

        Page<CommentDto> commentPage = new PageImpl<>(Collections.singletonList(commentDtoResponse),
                PageRequest.of(0, 20), 1);
        when(commentService.getTaskComments(1L, "NEWEST_FIRST", 0, 20)).thenReturn(commentPage);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/tasks/1/comments")
                        .principal(new TestingAuthenticationToken("email@email.ru", "Qwerty123",
                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))))
                        .param("sort", "NEWEST_FIRST")
                        .param("from", "0")
                        .param("size", "20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].text").value("Интересная задача"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].taskId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].author.id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].author.firstName").value("Дмитрий"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].author.lastName").value("Лодочкин"));
    }

    @Test
    @WithMockUser(username = "email@email.ru", authorities = "ROLE_USER")
    void shouldThrowBadRequestWhileGetTaskCommentsWhenSortParameterIsNotSupported() throws Exception {
        when(commentService.getTaskComments(1L, "HELLO", 0, 20)).thenThrow(IllegalArgumentException.class);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/tasks/1/comments")
                        .principal(new TestingAuthenticationToken("email@email.ru", "Qwerty123",
                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))))
                        .param("sort", "HELLO")
                        .param("from", "0")
                        .param("size", "20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
