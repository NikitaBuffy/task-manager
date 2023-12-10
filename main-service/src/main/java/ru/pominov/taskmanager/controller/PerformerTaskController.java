package ru.pominov.taskmanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.pominov.taskmanager.dto.TaskDto;
import ru.pominov.taskmanager.model.enums.TaskStatus;
import ru.pominov.taskmanager.security.model.UserInfo;
import ru.pominov.taskmanager.security.service.UserInfoService;
import ru.pominov.taskmanager.service.TaskService;

import java.security.Principal;

@Tag(name = "Performer: Задачи", description = "API для работы с задачами для исполнителя")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/performer/tasks")
public class PerformerTaskController {

    private final TaskService taskService;

    private final UserInfoService userInfoService;

    @Operation(summary = "Изменение статуса задачи исполнителем", description = "Доступ для аутентифицированного пользователя - ROLE_USER.",
            security = {@SecurityRequirement(name = "BearerToken")})
    @ApiResponse(responseCode = "200", description = "Статус задачи обновлен", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = TaskDto.class)))
    @PatchMapping("/{taskId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<TaskDto> changeTaskStatusByPerformer(@Parameter(description = "Идентификатор задачи")
                                                               @Positive @PathVariable Long taskId,
                                                               @Parameter(description = "Статус задачи: WAITING, IN_PROGRESS, DONE")
                                                               @RequestParam String status,
                                                               Principal principal) {
        UserInfo userInfo = userInfoService.getUserInfo(principal.getName());
        TaskStatus taskStatus = TaskStatus.valueOf(status);
        return ResponseEntity.ok().body(taskService.changeTaskStatusByPerformer(taskId, taskStatus, userInfo.getId()));
    }
}
