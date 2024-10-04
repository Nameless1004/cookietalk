package com.sparta.cookietalk.auth.dto;

import com.sparta.cookietalk.auth.dto.AuthRequest.Signin;
import com.sparta.cookietalk.auth.dto.AuthRequest.Signup;
import com.sparta.cookietalk.common.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public sealed interface AuthRequest permits Signup, Signin {
    record Signin (
        @NotBlank String username,
        @NotBlank String password) implements AuthRequest {}

    record Signup (
        @NotBlank String username,
        @NotBlank String password,
        @Email String email,
        @NotBlank String nickname,
        @NotNull UserRole userRole
        ) implements AuthRequest {
    }
}
