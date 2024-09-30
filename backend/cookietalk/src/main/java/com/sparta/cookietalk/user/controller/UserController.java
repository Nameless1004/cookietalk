package com.sparta.cookietalk.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.cookietalk.common.dto.ResponseDto;
import com.sparta.cookietalk.common.enums.TokenType;
import com.sparta.cookietalk.kakao.KakaoLoginResponseDto;
import com.sparta.cookietalk.kakao.KakaoService;
import com.sparta.cookietalk.security.JwtUtil;
import com.sparta.cookietalk.user.dto.UserRequest;
import com.sparta.cookietalk.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private final KakaoService kakaoService;
    private final JwtUtil jwtUtil;

    @PostMapping("/users/signup")
    public ResponseEntity<ResponseDto<Void>> signup(@RequestBody @Valid UserRequest.Signup requestDto) {
        userService.signup(requestDto);

        return ResponseDto.toEntity(HttpStatus.CREATED, "성공적으로 회원가입이 완료되었습니다.", null);
    }

    @PostMapping("/users/logout")
    public ResponseEntity<?> logout(@RequestHeader(JwtUtil.AUTHORIZATION_HEADER) String accessToken) {
        return ResponseDto.toEntity(userService.logout(accessToken));
    }

    @GetMapping("/users/kakao/callback")
    public String kakoLogin(@RequestParam("code") String code,  HttpServletResponse response)
        throws JsonProcessingException {
        KakaoLoginResponseDto token = kakaoService.kakaoLogin(code);
        System.out.println("code = " + code);
        Cookie cookie = new Cookie("REFRESH", token.getRefreshToken());
        cookie.setPath("/");
        jwtUtil.addTokenToHeader(response, token.getAccessToken());
        jwtUtil.addCookie(response, TokenType.REFRESH, token.getRefreshToken());
        return "redirect:/";
    }
}
