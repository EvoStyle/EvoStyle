package com.example.evostyle.global.exception;

public class ExternalApiException extends CustomRuntimeException{
    public ExternalApiException(ErrorCode errorCode) {
        super(errorCode);
    }
}
