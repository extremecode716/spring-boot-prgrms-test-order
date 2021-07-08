package com.github.prgrms.errors;

public class EmptyRequestException extends RuntimeException{

    public EmptyRequestException(String message) {
        super(message);
    }

    public EmptyRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
