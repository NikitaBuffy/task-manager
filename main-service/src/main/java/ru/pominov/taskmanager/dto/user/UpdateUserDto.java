package ru.pominov.taskmanager.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Новые данные пользователя")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {

    @Schema(description = "Имя", example = "Никита")
    @Size(max = 50)
    private String firstName;

    @Schema(description = "Фамилия", example = "Поминов")
    @Size(max = 50)
    private String lastName;
}
