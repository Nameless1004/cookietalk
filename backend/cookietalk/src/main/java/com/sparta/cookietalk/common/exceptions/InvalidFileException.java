package com.sparta.cookietalk.common.exceptions;

public class InvalidFileException extends IllegalArgumentException{
    public InvalidFileException(String message){
        super(message);
    }
}
