package com.c99.innovation.exception;

import static com.c99.innovation.common.constant.Constants.RECORD_NOT_FOUND;

public class RecordNotFoundException extends RuntimeException {

    public RecordNotFoundException() {
        super(RECORD_NOT_FOUND);
    }

    public RecordNotFoundException(String message) {
        super(message);
    }
}
