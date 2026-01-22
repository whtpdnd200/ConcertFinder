package com.concertfinder.concertfinder.exception.custom_exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
