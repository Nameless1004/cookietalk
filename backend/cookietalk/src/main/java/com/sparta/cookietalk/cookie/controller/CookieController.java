package com.sparta.cookietalk.cookie.controller;

import com.sparta.cookietalk.common.dto.ResponseDto;
import com.sparta.cookietalk.cookie.dto.CookieRequest;
import com.sparta.cookietalk.cookie.dto.CookieResponse;
import com.sparta.cookietalk.cookie.service.CookieService;
import com.sparta.cookietalk.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class CookieController {

    private final CookieService cookieService;

    @GetMapping("/api/test")
    public String test() {
        return "upload";
    }

    @PostMapping("/api/channels/{channelId}/cookies")
    @ResponseBody
    public ResponseEntity<ResponseDto<CookieResponse.Create>> createCookie(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable("channelId") Long channelId,
        CookieRequest.Create create) {
        CookieResponse.Create cookie = cookieService.createCookie(userDetails.getUser(), channelId, create);
        return  ResponseDto.toEntity(HttpStatus.CREATED, cookie);
    }

    @GetMapping("/api/cookies/{cookieId}")
    public ResponseEntity<ResponseDto<CookieResponse.Detail>> getCookieById(@PathVariable("cookieId") Long cookieId){
        CookieResponse.Detail details = cookieService.getCookie(cookieId);
        return ResponseDto.toEntity(HttpStatus.OK, details);
    }
}
