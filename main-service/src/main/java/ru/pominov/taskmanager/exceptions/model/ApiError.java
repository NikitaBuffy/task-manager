package ru.pominov.taskmanager.exceptions.model;

import lombok.*;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ApiError {

    private HttpStatus status;

    private String reason;

    private String message;

    private String timestamp;
}
