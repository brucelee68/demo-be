package com.c99.innovation.exception;

import static com.c99.innovation.common.constant.Constants.ERROR_WHEN_UPLOAD_TO_S3;

public class UploadAttachmentException extends RuntimeException {

    public UploadAttachmentException() {
        super(ERROR_WHEN_UPLOAD_TO_S3);
    }

    public UploadAttachmentException(String message) {
        super(message);
    }
}
