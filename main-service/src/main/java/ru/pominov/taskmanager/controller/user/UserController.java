package ru.pominov.taskmanager.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.pominov.taskmanager.dto.user.UpdateUserDto;
import ru.pominov.taskmanager.dto.user.UserDto;
import ru.pominov.taskmanager.security.model.UserInfo;
import ru.pominov.taskmanager.security.service.UserInfoService;
import ru.pominov.taskmanager.service.user.UserService;

import java.security.Principal;

@Tag(name = "Пользователь", description = "API для работы с данными пользователя")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/user")
public class UserController {

    private final UserService userService;

    private final UserInfoService userInfoService;

    @Operation(summary = "Изменение данных пользователя (имени, фамилии)",
            description = "Доступ для аутентифицированного пользователя - ROLE_USER. Доступ только для автора. ",
            security = {@SecurityRequirement(name = "BearerToken")})
    @ApiResponse(responseCode = "200", description = "Данные пользователя обновлены", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = UserDto.class)))
    @PatchMapping()
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<UserDto> editUser(@Valid @RequestBody UpdateUserDto updateUserDto, Principal principal) {
        UserInfo userInfo = userInfoService.getUserInfo(principal.getName());
        return ResponseEntity.ok().body(userService.editUser(userInfo.getId(), updateUserDto));
    }
}
