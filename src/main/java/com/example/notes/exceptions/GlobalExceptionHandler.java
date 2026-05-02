package com.example.notes.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiErrorResponse> handleBaseException(BaseException e) {
        log.warn("Business exception occurred: {} - {}", e.getErrorCode().getCode(), e.getMessage());
        
        ErrorCode errorCode = e.getErrorCode();
        ApiErrorResponse response = ApiErrorResponse.of(
                errorCode,
                e.getMessage(),
                errorCode.getHttpStatus().value()
        );
        
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.warn("Validation failed for request");
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorCode errorCode = ErrorCode.VALIDATION_FAILED;
        ApiErrorResponse response = ApiErrorResponse.of(
                errorCode,
                errorCode.getDefaultMessage(),
                errorCode.getHttpStatus().value(),
                errors
        );

        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception e) {
        log.error("Unhandled exception occurred", e);
        
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        ApiErrorResponse response = ApiErrorResponse.of(
                errorCode,
                "An internal error occurred. Please contact support if the problem persists.",
                errorCode.getHttpStatus().value()
        );
        
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }
}