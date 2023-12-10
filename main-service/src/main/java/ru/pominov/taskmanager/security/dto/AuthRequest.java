package ru.pominov.taskmanager.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Пользовательские данные для входа")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {

    @Schema(description = "Почта", example = "mail@mail.ru")
    @NotBlank
    @NotNull
    @Email
    private String email;

    @Schema(description = "Пароль", example = "Qwerty123")
    @NotBlank
    @NotNull
    private String password;
}
