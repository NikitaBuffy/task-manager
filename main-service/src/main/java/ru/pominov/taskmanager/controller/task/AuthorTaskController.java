package ru.pominov.taskmanager.controller.task;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.pominov.taskmanager.dto.task.NewTaskDto;
import ru.pominov.taskmanager.dto.task.TaskDto;
import ru.pominov.taskmanager.dto.task.UpdateTaskDto;
import ru.pominov.taskmanager.security.model.UserInfo;
import ru.pominov.taskmanager.security.service.UserInfoService;
import ru.pominov.taskmanager.service.task.TaskService;

import java.security.Principal;

@Tag(name = "Author: Задачи", description = "API для работы с задачами для пользователя")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/author/tasks")
public class AuthorTaskController {

    private final TaskService taskService;

    private final UserInfoService userInfoService;

    @Operation(summary = "Создание новой задачи", description = "Доступ для аутентифицированного пользователя - ROLE_USER. " +
            "Доступ только для автора.",
            security = {@SecurityRequirement(name = "BearerToken")})
    @ApiResponse(responseCode = "201", description = "Задача создана", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = TaskDto.class)))
    @PostMapping("/new")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<TaskDto> addNewTask(@Valid @RequestBody NewTaskDto newTaskDto, Principal principal) {
        UserInfo userInfo = userInfoService.getUserInfo(principal.getName());
        return ResponseEntity.status(201).body(taskService.addNewTask(newTaskDto, userInfo.getId()));
    }

    @Operation(summary = "Редактирование своей задачи, изменение статуса и назначение исполнителя",
            description = "Доступ для аутентифицированного пользователя - ROLE_USER. Доступ только для автора. " +
            "Редактировать можно название, описание и приоритет задачи. Можно изменить статус и назначить исполнителя",
            security = {@SecurityRequirement(name = "BearerToken")})
    @ApiResponse(responseCode = "200", description = "Данные задачи обновлены", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = TaskDto.class)))
    @PatchMapping("/{taskId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<TaskDto> editTask(@Parameter(description = "Идентификатор задачи")
                                            @Positive @PathVariable Long taskId,
                                            @Valid @RequestBody UpdateTaskDto updateTaskDto,
                                            Principal principal) {
        UserInfo userInfo = userInfoService.getUserInfo(principal.getName());
        return ResponseEntity.ok().body(taskService.editTask(taskId, updateTaskDto, userInfo.getId()));
    }

    @Operation(summary = "Удаление своей задачи", description = "Доступ для аутентифицированного пользователя - ROLE_USER. " +
            "Доступ только для автора.",
            security = {@SecurityRequirement(name = "BearerToken")})
    @ApiResponse(responseCode = "204", description = "Задача удалена", content = @Content(mediaType = "application/json"))
    @DeleteMapping("/{taskId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Void> deleteTask(@Parameter(description = "Идентификатор задачи")
                                           @Positive @PathVariable Long taskId,
                                           Principal principal) {
        UserInfo userInfo = userInfoService.getUserInfo(principal.getName());
        taskService.deleteTask(taskId, userInfo.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Просмотр своих задач", description = "Доступ для аутентифицированного пользователя - ROLE_USER. " +
            "Доступ только для автора. Обеспечивается фильтрация и пагинация вывода.",
            security = {@SecurityRequirement(name = "BearerToken")})
    @ApiResponse(responseCode = "200", description = "Список своих задач", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Page.class, subTypes = {TaskDto.class})))
    @GetMapping()
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Page<TaskDto>> getAuthorTasks(Principal principal,
                                                    @Parameter(description = "Значение сортировки: DESC_ID, ASC_ID")
                                                    @RequestParam(defaultValue = "ASC_ID") String sort,
                                                    @Parameter(description = "Номер страницы")
                                                    @RequestParam(value = "from", defaultValue = "0")
                                                    @PositiveOrZero Integer from,
                                                    @Parameter(description = "Количество элементов на странице")
                                                    @RequestParam(value = "size", defaultValue = "20")
                                                    @Positive Integer size) {
        UserInfo userInfo = userInfoService.getUserInfo(principal.getName());
        return ResponseEntity.ok().body(taskService.getAuthorTasks(userInfo.getId(), sort, from, size));
    }

    @Operation(summary = "Просмотр своей конкретной задачи", description = "Доступ для аутентифицированного пользователя - ROLE_USER. " +
            "Доступ только для автора.",
            security = {@SecurityRequirement(name = "BearerToken")})
    @ApiResponse(responseCode = "200", description = "Найденная задача", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = TaskDto.class)))
    @GetMapping("/{taskId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<TaskDto> getAuthorTaskById(@Parameter(description = "Идентификатор задачи")
                                                     @Positive @PathVariable Long taskId,
                                                     Principal principal) {
        UserInfo userInfo = userInfoService.getUserInfo(principal.getName());
        return ResponseEntity.ok().body(taskService.getAuthorTaskById(taskId, userInfo.getId()));
    }
}
