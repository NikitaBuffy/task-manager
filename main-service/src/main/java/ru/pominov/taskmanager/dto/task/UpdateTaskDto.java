package ru.pominov.taskmanager.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Редактирование задачи, изменение статуса и назначение исполнителя")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskDto {

    @Schema(description = "Название задачи")
    @Size(max = 100)
    private String title;

    @Schema(description = "Описание задачи")
    @Size(max = 2000)
    private String description;

    @Schema(description = "Приоритет: LOW, MEDIUM, HIGH")
    private String priority;

    @Schema(description = "Статус задачи: WAITING, IN_PROGRESS, DONE")
    private String status;

    @Schema(description = "Назначение исполнителя задачи")
    private Long performerId;
}
