package com.concertfinder.concertfinder.exceptionHandler.customException;

public class UnAuthorizedException extends RuntimeException {

    public UnAuthorizedException(String message) {
        super(message);
    }
}
