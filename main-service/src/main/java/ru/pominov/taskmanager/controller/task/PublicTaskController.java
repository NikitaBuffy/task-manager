package ru.pominov.taskmanager.controller.task;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.pominov.taskmanager.dto.task.TaskDto;
import ru.pominov.taskmanager.service.task.TaskService;


@Tag(name = "Public: Задачи", description = "API для просмотра задач конкретного автора или исполнителя")
@RestController
@RequiredArgsConstructor
@RequestMapping()
public class PublicTaskController {

    private final TaskService taskService;

    @Operation(summary = "Просмотр задач конкретного автора", description = "Доступ для аутентифицированного пользователя - ROLE_USER. " +
            "Обеспечивается фильтрация и пагинация вывода.",
            security = {@SecurityRequirement(name = "BearerToken")})
    @ApiResponse(responseCode = "200", description = "Список задач автора", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Page.class, subTypes = {TaskDto.class})))
    @GetMapping("/author/{authorId}/tasks")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Page<TaskDto>> getTasksByAuthorId(@Parameter(description = "Идентификатор автора")
                                                            @Positive @PathVariable Long authorId,
                                                            @Parameter(description = "Значение сортировки: DESC_ID, ASC_ID")
                                                            @RequestParam(defaultValue = "ASC_ID") String sort,
                                                            @Parameter(description = "Номер страницы")
                                                            @RequestParam(value = "from", defaultValue = "0")
                                                            @PositiveOrZero Integer from,
                                                            @Parameter(description = "Количество элементов на странице")
                                                            @RequestParam(value = "size", defaultValue = "20")
                                                            @Positive Integer size) {
        return ResponseEntity.ok().body(taskService.getAuthorTasks(authorId, sort, from, size));
    }

    @Operation(summary = "Просмотр конкретной задачи автора", description = "Доступ для аутентифицированного пользователя - ROLE_USER.",
            security = {@SecurityRequirement(name = "BearerToken")})
    @ApiResponse(responseCode = "200", description = "Задача", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = TaskDto.class)))
    @GetMapping("/author/{authorId}/tasks/{taskId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<TaskDto> getAuthorTaskById(@Parameter(description = "Идентификатор автора")
                                                     @Positive @PathVariable Long authorId,
                                                     @Parameter(description = "Идентификатор задачи")
                                                     @Positive @PathVariable Long taskId) {
        return ResponseEntity.ok().body(taskService.getAuthorTaskById(taskId, authorId));
    }

    @Operation(summary = "Просмотр задач исполнителя", description = "Доступ для аутентифицированного пользователя - ROLE_USER. " +
            "Обеспечивается фильтрация и пагинация вывода.",
            security = {@SecurityRequirement(name = "BearerToken")})
    @ApiResponse(responseCode = "200", description = "Список задач исполнителя", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Page.class, subTypes = {TaskDto.class})))
    @GetMapping("/performer/{performerId}/tasks")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Page<TaskDto>> getTasksByPerformerId(@Parameter(description = "Идентификатор исполнителя")
                                                            @Positive @PathVariable Long performerId,
                                                            @Parameter(description = "Значение сортировки: DESC_ID, ASC_ID")
                                                            @RequestParam(defaultValue = "ASC_ID") String sort,
                                                            @Parameter(description = "Номер страницы")
                                                            @RequestParam(value = "from", defaultValue = "0")
                                                            @PositiveOrZero Integer from,
                                                            @Parameter(description = "Количество элементов на странице")
                                                            @RequestParam(value = "size", defaultValue = "20")
                                                            @Positive Integer size) {
        return ResponseEntity.ok().body(taskService.getPerformerTasks(performerId, sort, from, size));
    }

    @Operation(summary = "Просмотр конкретной задачи исполнителя", description = "Доступ для аутентифицированного пользователя - ROLE_USER.",
            security = {@SecurityRequirement(name = "BearerToken")})
    @ApiResponse(responseCode = "200", description = "Задача", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = TaskDto.class)))
    @GetMapping("/performer/{performerId}/tasks/{taskId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<TaskDto> getPerformerTaskById(@Parameter(description = "Идентификатор автора")
                                                     @Positive @PathVariable Long performerId,
                                                     @Parameter(description = "Идентификатор задачи")
                                                     @Positive @PathVariable Long taskId) {
        return ResponseEntity.ok().body(taskService.getPerformerTaskById(taskId, performerId));
    }
}
