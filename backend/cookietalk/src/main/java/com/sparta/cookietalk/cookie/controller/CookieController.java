package com.sparta.cookietalk.cookie.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.cookietalk.common.dto.Response;
import com.sparta.cookietalk.common.dto.Response.Slice;
import com.sparta.cookietalk.common.dto.ResponseDto;
import com.sparta.cookietalk.cookie.dto.CategorySearch;
import com.sparta.cookietalk.cookie.dto.CookieRequest;
import com.sparta.cookietalk.cookie.dto.CookieResponse;
import com.sparta.cookietalk.cookie.dto.CookieResponse.Create;
import com.sparta.cookietalk.cookie.dto.CookieResponse.List;
import com.sparta.cookietalk.cookie.dto.CookieSearch;
import com.sparta.cookietalk.cookie.service.CookieService;
import com.sparta.cookietalk.security.AuthUser;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/api/v1/cookies")
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

    @GetMapping("/api/v1/cookies/{cookieId}")
    public ResponseEntity<ResponseDto<CookieResponse.Detail>> getCookieById(@PathVariable("cookieId") Long cookieId){
        CookieResponse.Detail details = cookieService.getCookie(cookieId);
        return ResponseDto.toEntity(HttpStatus.OK, details);
    }

    @GetMapping("/api/v1/cookies/search")
    public ResponseEntity<ResponseDto<Response.Page<List>>> searchCookie(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) String keyword){

        Response.Page<List> listPage = cookieService.searchKeyword(page, size,
            CookieSearch.builder()
                .keyword(keyword)
                .build());
        return ResponseDto.toEntity(HttpStatus.OK, listPage);
    }

    @GetMapping("/api/v1/categories/{categoryId}/cookies")
    public ResponseEntity<ResponseDto<Response.Slice<List>>> getCookiesByCategoryId(
        @PathVariable("categoryId") Long categoryId,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam LocalDateTime startDateTime) {

        Response.Slice<List> slice = cookieService.getCookieListByCategory(size, startDateTime, CookieSearch.builder()
                .categoryId(categoryId)
                .build());
        return ResponseDto.toEntity(HttpStatus.OK, slice);
    }

    @GetMapping("/api/v1/users/{userId}/channel/cookies")
    public ResponseEntity<ResponseDto<Response.Page<List>>> getCookiesByUserId(@AuthenticationPrincipal AuthUser authUser, @PathVariable("userId") Long userId,
    @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size)
    {
        Response.Page<List> pageResult = cookieService.getCookieListByUserId(authUser, userId, page, size);
        return ResponseDto.toEntity(HttpStatus.OK, pageResult);
    }
}
