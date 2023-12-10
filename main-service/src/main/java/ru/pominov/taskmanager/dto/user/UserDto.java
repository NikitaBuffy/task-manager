package ru.pominov.taskmanager.dto.user;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Пользователь")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @Schema(description = "Идентификатор пользователя")
    private Long id;

    @Schema(description = "Имя")
    private String firstName;

    @Schema(description = "Фамилия")
    private String lastName;
}
