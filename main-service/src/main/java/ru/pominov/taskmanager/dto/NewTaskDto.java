package ru.pominov.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Новая задача")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewTaskDto {

    @Schema(description = "Название задачи", example = "Позаниматься английским")
    @NotBlank
    @Size(max = 100)
    private String title;

    @Schema(description = "Описание задачи", example = "Выучить слова 'в магазине', прочитать статью")
    @Size(max = 2000)
    private String description;

    @Schema(description = "Приоритет: LOW, MEDIUM, HIGH", example = "MEDIUM")
    @NotBlank
    private String priority;
}

