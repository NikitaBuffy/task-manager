package ru.pominov.taskmanager.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pominov.taskmanager.dto.user.UserDto;

@Schema(description = "Комментарий")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    @Schema(description = "Идентификатор комментария", example = "1")
    private Long id;

    @Schema(description = "Текст комментария", example = "Рекомендую изучить дополнительно фразовые глаголы")
    private String text;

    @Schema(description = "Идентификатор задачи, к которой оставлен комментарий", example = "2")
    private Long taskId;

    @Schema(description = "Автор комментария")
    private UserDto author;

    @Schema(description = "Дата и время комментария в формате (yyyy-MM-dd HH:mm:ss)", example = "2023-12-10 23:10:43")
    private String created;
}
