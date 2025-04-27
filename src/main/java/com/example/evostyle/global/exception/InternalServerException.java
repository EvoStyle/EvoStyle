package com.example.evostyle.global.exception;

public class InternalServerException extends CustomRuntimeException {
    public InternalServerException(ErrorCode errorCode) {
        super(errorCode);
    }
}
