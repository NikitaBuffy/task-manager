package ru.pominov.taskmanager.controller.comment;

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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.pominov.taskmanager.dto.comment.CommentDto;
import ru.pominov.taskmanager.dto.comment.NewCommentDto;
import ru.pominov.taskmanager.security.model.UserInfo;
import ru.pominov.taskmanager.security.service.UserInfoService;
import ru.pominov.taskmanager.service.comment.CommentService;

import java.security.Principal;

@Tag(name = "Комментарии", description = "API для работы с комментариями к задачам")
@RestController
@RequiredArgsConstructor
@RequestMapping
public class CommentController {

    private final CommentService commentService;
    private final UserInfoService userInfoService;

    @Operation(summary = "Добавление комментария к задаче", description = "Доступ для аутентифицированного пользователя - ROLE_USER.",
            security = {@SecurityRequirement(name = "BearerToken")})
    @ApiResponse(responseCode = "201", description = "Комментарий добавлен", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = CommentDto.class)))
    @PostMapping("/tasks/{taskId}/comment")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<CommentDto> addNewComment(@Parameter(description = "Идентификатор задачи")
                                              @Positive @PathVariable Long taskId,
                                              @Valid @RequestBody NewCommentDto newCommentDto,
                                              Principal principal) {
        UserInfo userInfo = userInfoService.getUserInfo(principal.getName());
        return ResponseEntity.status(201).body(commentService.addNewComment(taskId, newCommentDto, userInfo.getId()));
    }

    @Operation(summary = "Просмотр всех комментариев к задаче", description = "Доступ для аутентифицированного пользователя - ROLE_USER. " +
            "Обеспечивается фильтрация и пагинация вывода.",
            security = {@SecurityRequirement(name = "BearerToken")})
    @ApiResponse(responseCode = "200", description = "Список комментариев", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Page.class, subTypes = {CommentDto.class})))
    @GetMapping("/tasks/{taskId}/comments")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Page<CommentDto>> addNewComment(@Parameter(description = "Идентификатор задачи")
                                                          @Positive @PathVariable Long taskId,
                                                          @Parameter(description = "Значение сортировки: NEWEST_FIRST, OLDEST_FIRST")
                                                          @RequestParam(defaultValue = "NEWEST_FIRST") String sort,
                                                          @Parameter(description = "Номер страницы")
                                                          @RequestParam(value = "from", defaultValue = "0")
                                                          @PositiveOrZero Integer from,
                                                          @Parameter(description = "Количество элементов на странице")
                                                          @RequestParam(value = "size", defaultValue = "20")
                                                          @Positive Integer size) {
        return ResponseEntity.ok().body(commentService.getTaskComments(taskId, sort, from, size));
    }
}
