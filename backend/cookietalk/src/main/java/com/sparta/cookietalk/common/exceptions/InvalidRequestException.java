package com.sparta.cookietalk.common.exceptions;

public class InvalidRequestException extends IllegalArgumentException {

    public InvalidRequestException(String message) {
        super(message);
    }
}
