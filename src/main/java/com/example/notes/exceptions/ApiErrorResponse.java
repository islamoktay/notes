package com.example.notes.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiErrorResponse(
        LocalDateTime timestamp,
        int status,
        String errorCode,
        String message,
        Map<String, String> errors
) {
    public static ApiErrorResponse of(ErrorCode errorCode, String message, int status) {
        return new ApiErrorResponse(LocalDateTime.now(), status, errorCode.getCode(), message, null);
    }

    public static ApiErrorResponse of(ErrorCode errorCode, String message, int status, Map<String, String> errors) {
        return new ApiErrorResponse(LocalDateTime.now(), status, errorCode.getCode(), message, errors);
    }
}
