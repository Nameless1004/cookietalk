package com.sparta.cookietalk.common.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseDto<T>{
    public int statusCode;
    public String message;
    public T data;

    public static <T> ResponseDto<T> of(int statusCode, String message, T data) {
        return new ResponseDto<T>(statusCode, message, data);
    }

    public static <T> ResponseDto<T> of(int statusCode, T data) {
        return new ResponseDto<T>(statusCode, "", data);
    }

    public static <T> ResponseDto<T> of(int statusCode, String message) {
        return new ResponseDto<T>(statusCode, message, null);
    }

    public static <T> ResponseDto<T> of(int statusCode) {
        return new ResponseDto<T>(statusCode, "", null);
    }

    public static <T> ResponseDto<T> of(HttpStatus statusCode, String message, T data) {
        return new ResponseDto<T>(statusCode.value(), message, data);
    }

    public static <T> ResponseDto<T> of(HttpStatus statusCode, T data) {
        return new ResponseDto<T>(statusCode.value(), "", data);
    }

    public static <T> ResponseDto<T> of(HttpStatus statusCode, String message) {
        return new ResponseDto<T>(statusCode.value(), message, null);
    }

    public static <T> ResponseDto<T> of(HttpStatus statusCode) {
        return new ResponseDto<T>(statusCode.value(), "", null);
    }

    public static <T> ResponseEntity<ResponseDto<T>> toEntity(int statusCode, T data) {
        return ResponseEntity.status(statusCode).body(ResponseDto.of(statusCode, "", data));
    }

    public static <T> ResponseEntity<ResponseDto<T>> toEntity(int statusCode, String message, T data) {
        return ResponseEntity.status(statusCode).body(ResponseDto.of(statusCode, message, data));
    }

    public static <T> ResponseEntity<ResponseDto<T>> toEntity(int statusCode) {
        return ResponseEntity.status(statusCode).body(ResponseDto.of(statusCode, "", null));
    }

    public static <T> ResponseEntity<ResponseDto<T>> toEntity(HttpStatus statusCode, T data) {
        return ResponseEntity.status(statusCode).body(ResponseDto.of(statusCode, "", data));
    }

    public static <T> ResponseEntity<ResponseDto<T>> toEntity(HttpStatus statusCode, String message, T data) {
        return ResponseEntity.status(statusCode).body(ResponseDto.of(statusCode, message, data));
    }

    public static <T> ResponseEntity<ResponseDto<T>> toEntity(HttpStatus statusCode) {
        return ResponseEntity.status(statusCode).body(ResponseDto.of(statusCode, "", null));
    }

    public static <T> ResponseEntity<ResponseDto<T>> toEntity(ResponseDto<T> dto) {
        return ResponseEntity.status(dto.getStatusCode()).body(dto);
    }
}


