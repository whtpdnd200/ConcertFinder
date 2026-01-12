package com.concertfinder.concertfinder.exception.custom_exception;

public class DuplicateException extends RuntimeException{

    public DuplicateException(String message) {
        super(message);
    }
}
