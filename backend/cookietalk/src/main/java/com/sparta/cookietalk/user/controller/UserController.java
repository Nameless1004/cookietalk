package com.sparta.cookietalk.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.cookietalk.auth.dto.AuthResponse;
import com.sparta.cookietalk.common.dto.ResponseDto;
import com.sparta.cookietalk.kakao.KakaoService;
import com.sparta.cookietalk.security.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final KakaoService kakaoService;
    private final JwtUtil jwtUtil;

    @GetMapping("/users/kakao/callback")
    public ResponseEntity<ResponseDto<AuthResponse.KakaoLogin>> kakoLogin(@RequestParam("code") String code,  HttpServletResponse response)
        throws JsonProcessingException {
        AuthResponse.KakaoLogin token = kakaoService.kakaoLogin(code);
        return ResponseDto.toEntity(HttpStatus.OK, token);
    }
}
