package ru.pominov.taskmanager.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import ru.pominov.taskmanager.security.dto.AuthRequest;
import ru.pominov.taskmanager.security.dto.AuthResponse;
import ru.pominov.taskmanager.security.dto.UserInfoRequestDto;
import ru.pominov.taskmanager.security.mapper.UserInfoMapper;
import ru.pominov.taskmanager.security.model.UserInfo;
import ru.pominov.taskmanager.security.service.JwtService;
import ru.pominov.taskmanager.security.service.UserInfoService;
import ru.pominov.taskmanager.security.dto.UserInfoResponseDto;

@Tag(name = "Регистрация/авторизация", description = "API для пользовательской регистрации, авторизации")
@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class UserInfoController {

    private final UserInfoService userInfoService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserInfoMapper userInfoMapper;

    @Operation(summary = "Регистрация пользователя", description = "Доступ для всех")
    @PostMapping("/registration")
    public UserInfoResponseDto addNewUser(@RequestBody @Valid UserInfoRequestDto userInfoRequestDto) {
        UserInfo userInfo = userInfoMapper.userRequestDtoToUserInfo(userInfoRequestDto);
        userInfo.setRoles("ROLE_USER");
        userInfoService.addUser(userInfo);
        return userInfoMapper.userInfoToUserResponseDto(userInfo);
    }

    @Operation(summary = "Вход пользователя", description = "Доступ для всех")
    @PostMapping("/login")
    public AuthResponse authenticateAndGetToken(@RequestBody @Valid AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            UserInfo userInfo = userInfoService.getUserInfo(authRequest.getEmail());
            return AuthResponse.builder()
                    .token(jwtService.generateToken(authRequest.getEmail()))
                    .role(userInfo.getRoles())
                    .build();
        } else {
            throw new UsernameNotFoundException("Invalid user request.");
        }
    }
}
