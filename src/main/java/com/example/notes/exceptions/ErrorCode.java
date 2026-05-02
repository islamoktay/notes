package com.example.notes.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 400 - Bad Request
    INVALID_INPUT("INVALID_INPUT", "The input data is invalid", HttpStatus.BAD_REQUEST),
    VALIDATION_FAILED("VALIDATION_FAILED", "Validation failed for one or more fields", HttpStatus.BAD_REQUEST),
    
    // 404 - Not Found
    USER_NOT_FOUND("USER_NOT_FOUND", "The requested user was not found", HttpStatus.NOT_FOUND),
    NOTE_NOT_FOUND("NOTE_NOT_FOUND", "The requested note was not found", HttpStatus.NOT_FOUND),
    RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND", "The requested resource was not found", HttpStatus.NOT_FOUND),

    // 500 - Internal Server Error
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String defaultMessage;
    private final HttpStatus httpStatus;

    ErrorCode(String code, String defaultMessage, HttpStatus httpStatus) {
        this.code = code;
        this.defaultMessage = defaultMessage;
        this.httpStatus = httpStatus;
    }
}
