package com.example.notes.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 400 - Bad Request
    INVALID_INPUT("INVALID_INPUT", "error.invalid_input", "The input data is invalid", HttpStatus.BAD_REQUEST),
    VALIDATION_FAILED("VALIDATION_FAILED", "error.validation_failed", "Validation failed for one or more fields", HttpStatus.BAD_REQUEST),
    INVALID_SORT_FIELD("INVALID_SORT_FIELD", "error.invalid_sort_field", "The specified sort field is not valid", HttpStatus.BAD_REQUEST),

    // 409 - Conflict
    EMAIL_ALREADY_IN_USE("EMAIL_ALREADY_IN_USE", "error.email_already_in_use", "This email address is already registered", HttpStatus.CONFLICT),

    // 404 - Not Found
    USER_NOT_FOUND("USER_NOT_FOUND", "error.user_not_found", "The requested user was not found", HttpStatus.NOT_FOUND),
    NOTE_NOT_FOUND("NOTE_NOT_FOUND", "error.note_not_found", "The requested note was not found", HttpStatus.NOT_FOUND),
    RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND", "error.resource_not_found", "The requested resource was not found", HttpStatus.NOT_FOUND),

    // 500 - Internal Server Error
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "error.internal_server_error", "An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String messageKey;
    private final String defaultMessage;
    private final HttpStatus httpStatus;

    ErrorCode(String code, String messageKey, String defaultMessage, HttpStatus httpStatus) {
        this.code = code;
        this.messageKey = messageKey;
        this.defaultMessage = defaultMessage;
        this.httpStatus = httpStatus;
    }
}
