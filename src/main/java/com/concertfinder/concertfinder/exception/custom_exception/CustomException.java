package com.concertfinder.concertfinder.exception.custom_exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    public CustomException(String message) {
        super(message);
    }
}

