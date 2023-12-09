package ru.pominov.taskmanager.security.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoResponseDto {

    private Long id;

    private String email;

    private String password;

    private String roles;
}
