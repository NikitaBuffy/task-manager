package ru.pominov.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pominov.taskmanager.model.enums.TaskPriority;
import ru.pominov.taskmanager.model.enums.TaskStatus;

@Schema(description = "Задача")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {

    @Schema(description = "Идентификатор задачи")
    private Long id;

    @Schema(description = "Название задачи")
    private String title;

    @Schema(description = "Описание задачи")
    private String description;

    @Schema(description = "Статус: WAITING, IN_PROGRESS, DONE")
    private TaskStatus status;

    @Schema(description = "Приоритет: LOW, MEDIUM, HIGH")
    private TaskPriority priority;

    @Schema(description = "Автор задачи")
    private UserDto author;

    @Schema(description = "Исполнитель задачи")
    private UserDto performer;

//    private List<CommentDto> commentDtoList;
}
