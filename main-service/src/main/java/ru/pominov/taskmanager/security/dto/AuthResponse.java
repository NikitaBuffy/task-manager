package ru.pominov.taskmanager.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Ответ при успешной авторизации")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {

    @Schema(description = "JWT-токен", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYWlsQG1haWwucnUiLCJpYXQiOjE3MDIxNjA2NzQsImV4cCI6MTcwMjE2MjQ3NH0.4_Gx-HN9jhK_19UzU0T-y9dgRdY1_eYrMvT1FH879Ag")
    String token;

    @Schema(description = "Роль (ROLE_USER)", example = "ROLE_USER")
    String role;
}
