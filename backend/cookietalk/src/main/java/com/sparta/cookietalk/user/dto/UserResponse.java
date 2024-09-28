package com.sparta.cookietalk.user.dto;

import com.sparta.cookietalk.user.dto.UserResponse.Login;
import com.sparta.cookietalk.user.dto.UserResponse.Reissue;
import com.sparta.cookietalk.user.entity.User;

public sealed interface UserResponse permits Login, Reissue {

    record Login(String userId, String userNickname, String accessToken,
                 String refreshToken) implements UserResponse {
        public Login(User user, String access, String refresh) {
            this(user.getUsername(), user.getNickname(), access, refresh);
        }
    }

    record Reissue(String accessToken, String refreshToken) implements UserResponse {}
}