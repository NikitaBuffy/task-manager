package ru.pominov.taskmanager.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.pominov.taskmanager.dto.user.UpdateUserDto;
import ru.pominov.taskmanager.security.model.UserInfo;
import ru.pominov.taskmanager.security.service.UserInfoService;
import ru.pominov.taskmanager.service.user.UserService;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
@SpringBootTest
@RequiredArgsConstructor
@ActiveProfiles("test")
public class UserServiceIntegrationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserInfoService userInfoService;

    private UserInfo userInfoOne;

    @BeforeEach
    void beforeEach() {
        userInfoOne = new UserInfo(1L, "user13@mail.ru", "Qwerty123", "ROLE_USER");
    }

    @Test
    void shouldChangeUserFirstNameAndLastName() {
        userInfoService.addUser(userInfoOne);
        assertThat(userService.editUser(1L, new UpdateUserDto("Никита", "Поминов")))
                .hasFieldOrPropertyWithValue("firstName", "Никита")
                .hasFieldOrPropertyWithValue("lastName", "Поминов");
    }

    @Test
    void shouldChangeUserFirstName() {
        userInfoService.addUser(userInfoOne);
        assertThat(userService.editUser(1L, new UpdateUserDto("Евгений", null)))
                .hasFieldOrPropertyWithValue("firstName", "Евгений")
                .hasFieldOrProperty("lastName");
    }

    @Test
    void shouldChangeUserLastName() {
        userInfoService.addUser(userInfoOne);
        assertThat(userService.editUser(1L, new UpdateUserDto(null, "Ассертов")))
                .hasFieldOrProperty("firstName")
                .hasFieldOrPropertyWithValue("lastName", "Ассертов");
    }
}
