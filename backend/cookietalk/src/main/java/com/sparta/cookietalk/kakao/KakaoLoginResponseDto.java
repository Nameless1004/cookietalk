package com.sparta.cookietalk.kakao;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoLoginResponseDto {
    private String accessToken;
    private String refreshToken;
}

