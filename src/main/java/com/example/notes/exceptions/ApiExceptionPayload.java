package com.example.notes.exceptions;

import org.springframework.http.HttpStatus;
import java.time.ZonedDateTime;

public record ApiExceptionPayload(
        String message,
        HttpStatus httpStatus,
        ZonedDateTime timestamp
) {}