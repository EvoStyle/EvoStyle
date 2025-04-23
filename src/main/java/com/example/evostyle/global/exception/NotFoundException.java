package com.example.evostyle.global.exception;

public class NotFoundException extends CustomRuntimeException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
