package com.sparta.cookietalk.auth.dto;

import com.sparta.cookietalk.auth.dto.AuthRequest.CheckEmail;
import com.sparta.cookietalk.auth.dto.AuthRequest.CheckNickname;
import com.sparta.cookietalk.auth.dto.AuthRequest.CheckUsername;
import com.sparta.cookietalk.auth.dto.AuthRequest.Signin;
import com.sparta.cookietalk.auth.dto.AuthRequest.Signup;
import com.sparta.cookietalk.common.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public sealed interface AuthRequest permits Signup, Signin, CheckNickname, CheckEmail,
    CheckUsername {
    record Signin (
        @NotBlank String username,
        @NotBlank String password,
        String adminToken) implements AuthRequest {}

    record Signup (
        @NotBlank String username,
        @NotBlank String password,
        @Email String email,
        @NotBlank String nickname,
        String adminToken,
        @NotNull UserRole userRole
        ) implements AuthRequest {
    }

    record CheckNickname(String nickname) implements AuthRequest {}
    record CheckEmail(String email) implements AuthRequest {}
    record CheckUsername(String username) implements AuthRequest {}
}
