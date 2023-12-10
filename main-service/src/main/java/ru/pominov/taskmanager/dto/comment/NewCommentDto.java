package ru.pominov.taskmanager.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Новый комментарий")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewCommentDto {

    @Schema(description = "Текст комментарий", example = "Рекомендую изучить дополнительно фразовые глаголы")
    @NotBlank
    @Size(max = 500)
    private String text;
}
