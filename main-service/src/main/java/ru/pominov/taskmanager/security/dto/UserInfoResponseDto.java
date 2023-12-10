package ru.pominov.taskmanager.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "Данные ответа после успешной регистрации")
@Data
@Builder
public class UserInfoResponseDto {

    @Schema(description = "Идентификатор пользователя", example = "1")
    private Long id;

    @Schema(description = "Почта", example = "mail@mail.ru")
    private String email;

    @Schema(description = "Пароль (закодированный)", example = "$2a$10$kXE5TpvZ0CH8i.0ywK/MV.3BD6uP6.oLXzKx/PhvZmIVuRZOY2eb.")
    private String password;

    @Schema(description = "Роль (ROLE_USER)", example = "ROLE_USER")
    private String roles;
}
