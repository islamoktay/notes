package com.example.notes.exceptions;

public class ApiRequestException extends BaseException {
    public ApiRequestException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ApiRequestException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}