package com.c99.innovation.exception;

public class UserNameExistsException extends RuntimeException {

    public UserNameExistsException(String message) {
        super(message);
    }
}
