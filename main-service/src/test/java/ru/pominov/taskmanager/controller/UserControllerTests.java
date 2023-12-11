package ru.pominov.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.pominov.taskmanager.dto.user.UpdateUserDto;
import ru.pominov.taskmanager.dto.user.UserDto;
import ru.pominov.taskmanager.security.model.UserInfo;
import ru.pominov.taskmanager.security.service.UserInfoService;
import ru.pominov.taskmanager.service.user.UserService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@RequiredArgsConstructor
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    UserService userService;

    @MockBean
    UserInfoService userInfoService;

    UserDto userDtoResponse = new UserDto(1L, "Никита", "Пименов");
    UpdateUserDto updateUserDtoWithSizeMoreThanMax = new UpdateUserDto("Напу Амо Хала Она Она Анека Вехи Вехи Она Хивеа Нена Вава Кехо Онка Кахе Хеа", null);
    UpdateUserDto updateUserDto = new UpdateUserDto(null, "Пименов");
    UserInfo userInfo = new UserInfo(1L, "email@email.ru", "Qwerty123", "ROLE_USER");


    @Test
    @WithMockUser(username = "email@email.ru", authorities = "ROLE_USER")
    void shouldThrowBadRequestWhenUserNameMoreThan50() throws Exception {
        when(userInfoService.getUserInfo("email@email.ru")).thenReturn(userInfo);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/user")
                        .content(mapper.writeValueAsString(updateUserDtoWithSizeMoreThanMax))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "email@email.ru", authorities = "ROLE_USER")
    void shouldUpdateUserData() throws Exception {
        when(userInfoService.getUserInfo("email@email.ru")).thenReturn(userInfo);
        when(userService.editUser(1L, updateUserDto)).thenReturn(userDtoResponse);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/user")
                        .content(mapper.writeValueAsString(updateUserDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Никита"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Пименов"));
    }

}
