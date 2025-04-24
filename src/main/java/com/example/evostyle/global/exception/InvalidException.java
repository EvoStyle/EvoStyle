package com.example.evostyle.global.exception;

public class InvalidException extends CustomRuntimeException {
    public InvalidException(ErrorCode errorCode) {
        super(errorCode);
    }
}
