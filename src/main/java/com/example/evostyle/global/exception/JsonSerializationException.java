package com.example.evostyle.global.exception;

public class JsonSerializationException extends CustomRuntimeException {
    public JsonSerializationException(ErrorCode message) {
        super(message);
    }
}
