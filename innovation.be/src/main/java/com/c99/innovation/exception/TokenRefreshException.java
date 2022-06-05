package com.c99.innovation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.c99.innovation.common.constant.Constants.FAILED_FOR_TOKEN_MESSAGE;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TokenRefreshException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TokenRefreshException(String token, String message) {
        super(String.format(FAILED_FOR_TOKEN_MESSAGE, token, message));
    }
}
