package com.sparta.cookietalk.common.exceptions;

import org.springframework.http.HttpStatus;

public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }

}
