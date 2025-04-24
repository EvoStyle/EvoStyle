package com.example.evostyle.global.exception;

public class UnauthorizedException extends CustomRuntimeException {
  public UnauthorizedException(ErrorCode errorCode) {
    super(errorCode);
  }
}
