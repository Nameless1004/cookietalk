package com.sparta.cookietalk.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.cookietalk.common.enums.TokenType;
import com.sparta.cookietalk.kakao.KakaoLoginResponseDto;
import com.sparta.cookietalk.kakao.KakaoService;
import com.sparta.cookietalk.security.JwtUtil;
import com.sparta.cookietalk.user.dto.SignupRequestDto;
import com.sparta.cookietalk.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private final KakaoService kakaoService;
    private final JwtUtil jwtUtil;

    @GetMapping("/users/login-page")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/users/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/users/signup")
    public String signup(@Valid SignupRequestDto requestDto, BindingResult bindingResult) {
        // Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            return "redirect:/api/users/signup";
        }

        userService.signup(requestDto);

        return "redirect:/api/users/login-page";
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
