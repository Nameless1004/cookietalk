package com.sparta.cookietalk.cookie.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.cookietalk.common.dto.ResponseDto;
import com.sparta.cookietalk.cookie.dto.CookieRequest;
import com.sparta.cookietalk.cookie.dto.CookieResponse;
import com.sparta.cookietalk.cookie.dto.CookieResponse.Create;
import com.sparta.cookietalk.cookie.dto.CookieResponse.Detail;
import com.sparta.cookietalk.cookie.service.CookieService;
import com.sparta.cookietalk.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class CookieController {

    private final CookieService cookieService;

    @GetMapping("/api/test")
    public String test() {
        return "upload";
    }

    @PostMapping("/api/cookies")
    @ResponseBody
    public ResponseEntity<ResponseDto<CookieResponse.Create>> createCookie(
        @AuthenticationPrincipal AuthUser authUser,
        @RequestPart CookieRequest.Create create,
        @RequestPart("video") MultipartFile video,
        @RequestPart("thumbnail") MultipartFile thumbnail,
        @RequestPart(value = "attachment", required = false) MultipartFile attachment)
        throws JsonProcessingException {

        CookieResponse.Create cookie = cookieService.createCookie(authUser, create, video, thumbnail, attachment);
        ObjectMapper mapper = new ObjectMapper();
        ResponseDto<Create> objectResponseDto = ResponseDto.of(HttpStatus.CREATED, cookie);
        String s = mapper.writeValueAsString(objectResponseDto);
        System.out.println("s = " + s);
        return  ResponseDto.toEntity(HttpStatus.CREATED, cookie);
    }

    @GetMapping("/api/cookies/{cookieId}")
    public ResponseEntity<ResponseDto<CookieResponse.Detail>> getCookieById(@PathVariable("cookieId") Long cookieId){
        CookieResponse.Detail details = cookieService.getCookie(cookieId);
        return ResponseDto.toEntity(HttpStatus.OK, details);
    }

    @GetMapping("/api/channel/{channelId}/cookies")
    public ResponseEntity<ResponseDto<Page<Detail>>> getCookiesByUserId(@AuthenticationPrincipal AuthUser authUser, @PathVariable("channelId") Long channelId,
    @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size)
    {
        Page<Detail> result = cookieService.getCookiesByChannelId(authUser,
            channelId, page, size);
        return ResponseDto.toEntity(HttpStatus.OK, result);
    }
}
