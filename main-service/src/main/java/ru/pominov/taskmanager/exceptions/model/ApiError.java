package ru.pominov.taskmanager.exceptions.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Schema(description = "Сведения об ошибке")
public class ApiError {

    @Schema(description = "Код статуса HTTP-ответа", example = "BAD_REQUEST")
    private HttpStatus status;

    @Schema(description = "Общее описание причины ошибки", example = "Incorrectly made request")
    private String reason;

    @Schema(description = "Сообщение об ошибке", example = "Validation failed for argument [email], must be not blank")
    private String message;

    @Schema(description = "Дата и время когда произошла ошибка в формате (yyyy-MM-dd HH:mm:ss)", example = "2023-12-10 09:10:50")
    private String timestamp;
}
