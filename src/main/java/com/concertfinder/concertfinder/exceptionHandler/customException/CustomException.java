package com.concertfinder.concertfinder.exceptionHandler.customException;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    public CustomException(String message) {
        super(message);
    }
}

