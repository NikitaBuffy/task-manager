package ru.pominov.taskmanager.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoRequestDto {

    @Email
    @NotBlank
    @NotNull
    private String email;

    @NotBlank
    @NotNull
    private String password;
}
