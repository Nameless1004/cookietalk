package com.sparta.cookietalk.common.exceptions;

public abstract class UploadException extends RuntimeException {
    public UploadException(String message) {
        super(message);
    }
}
