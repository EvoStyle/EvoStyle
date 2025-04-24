package com.example.evostyle.global.exception;

import com.example.evostyle.common.entity.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomRuntimeException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(final CustomRuntimeException e) {
        ErrorResponse response = new ErrorResponse(e.getErrorCode());
        return createResponseEntity(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException e
    ) {
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST,
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return createResponseEntity(response);
    }

    private ResponseEntity<ErrorResponse> createResponseEntity(ErrorResponse response) {
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }
}
