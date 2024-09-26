package com.sparta.cookietalk.reissue.controller;

import com.sparta.cookietalk.common.dto.ResponseDto;
import com.sparta.cookietalk.reissue.service.ReissueService;
import com.sparta.cookietalk.security.JwtUtil;
import com.sparta.cookietalk.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReissueController {

    private final ReissueService reissueService;

    @PostMapping("/api/users/reissue")
    public ResponseEntity<?> reissue(@RequestHeader(JwtUtil.REFRESH_TOKEN_HEADER) String refreshToken) {
        ResponseDto<?> reissue = reissueService.reissue(refreshToken);

        return ResponseDto.toEntity(reissue);
    }

}