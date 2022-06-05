package com.c99.innovation.exception;

import static com.c99.innovation.common.constant.Constants.NOT_VALID_IMAGE_EXTENSION;

public class NotValidImageExtensionException extends RuntimeException {

    public NotValidImageExtensionException() {
        super(NOT_VALID_IMAGE_EXTENSION);
    }

    public NotValidImageExtensionException(String message) {
        super(message);
    }
}
