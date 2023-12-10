package ru.pominov.taskmanager.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Schema(description = "Данные пользователя при регистрации")
@Data
@Builder
public class UserInfoRequestDto {

    @Schema(description = "Почта", example = "mail@mail.ru")
    @Email
    @NotBlank
    @NotNull
    private String email;

    @Schema(description = "Пароль", example = "Qwerty123")
    @NotBlank
    @NotNull
    private String password;
}
