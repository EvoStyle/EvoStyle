package com.example.evostyle.domain.parcel.exception;

public class ParcelAlreadyReceivedException extends RuntimeException {
    public ParcelAlreadyReceivedException(String message) {
        super(message);
    }
}
