package com.sparta.cookietalk.auth.dto;

import com.sparta.cookietalk.auth.dto.AuthResponse.CheckEmail;
import com.sparta.cookietalk.auth.dto.AuthResponse.CheckNickname;
import com.sparta.cookietalk.auth.dto.AuthResponse.CheckUsername;
import com.sparta.cookietalk.auth.dto.AuthResponse.KakaoLogin;
import com.sparta.cookietalk.auth.dto.AuthResponse.Reissue;
import com.sparta.cookietalk.auth.dto.AuthResponse.Signin;
import com.sparta.cookietalk.auth.dto.AuthResponse.Signup;
import com.sparta.cookietalk.user.entity.User;

public sealed interface AuthResponse permits Signup, Signin, Reissue, KakaoLogin, CheckEmail,
    CheckNickname, CheckUsername {
    record Signup( Long userId ) implements AuthResponse {
        public Signup(User newUser) {
            this(newUser.getId());
        }
    }

    record Signin(String userId, String userNickname, String accessToken,
                 String refreshToken) implements AuthResponse {
        public Signin(User user, String access, String refresh) {
            this(user.getUsername(), user.getNickname(), access, refresh);
        }
    }

    record Reissue(String accessToken, String refreshToken) implements AuthResponse {}
    record KakaoLogin(String accessToken, String refreshToken) implements AuthResponse {}

    record CheckNickname(boolean isNicknameDuplicate) implements AuthResponse {}
    record CheckEmail(boolean isEmailDuplicate) implements AuthResponse {}
    record CheckUsername(boolean isUsernameDuplicate) implements AuthResponse {}
}
