package com.sparta.cookietalk.user.dto;

import com.sparta.cookietalk.user.dto.UserRequest.Login;
import com.sparta.cookietalk.user.dto.UserRequest.Signup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public sealed interface UserRequest permits Login, Signup {
    record Login (
        @NotBlank String username,
        @NotBlank String password) implements UserRequest {}

    record Signup (
        @NotBlank String username,
        @NotBlank String password,
        @Email String email,
        @NotBlank String nickname,
        boolean admin,
        String adminToken) implements UserRequest {

    }
}
