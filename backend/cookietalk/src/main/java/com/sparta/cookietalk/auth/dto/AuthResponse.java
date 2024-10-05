package com.sparta.cookietalk.auth.dto;

import com.sparta.cookietalk.auth.dto.AuthResponse.DuplicateCheck;
import com.sparta.cookietalk.auth.dto.AuthResponse.KakaoLogin;
import com.sparta.cookietalk.auth.dto.AuthResponse.Reissue;
import com.sparta.cookietalk.auth.dto.AuthResponse.Signin;
import com.sparta.cookietalk.auth.dto.AuthResponse.Signup;
import com.sparta.cookietalk.user.entity.User;

public sealed interface AuthResponse permits Signup, Signin, Reissue, KakaoLogin, DuplicateCheck {
    record Signup( Long userId ) implements AuthResponse {
        public Signup(User newUser) {
            this(newUser.getId());
        }
    }

    record Signin(Long id, String userId, String userNickname, String accessToken,
                 String refreshToken) implements AuthResponse {
        public Signin(User user, String access, String refresh) {
            this(user.getId(), user.getUsername(), user.getNickname(), access, refresh);
        }
    }

    record Reissue(String accessToken, String refreshToken) implements AuthResponse {}
    record KakaoLogin(String accessToken, String refreshToken) implements AuthResponse {}

    record DuplicateCheck(boolean isDuplicated) implements AuthResponse {}
}
