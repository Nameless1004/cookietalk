package com.sparta.cookietalk.cookie.controller;

import com.sparta.cookietalk.cookie.service.CookieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
    public String test(){
        return "upload";
    }

//    @PostMapping("/api/cookies")
//    @ResponseBody
//    public ResponseEntity<Void> createCookie(@RequestPart("video")MultipartFile video, @RequestPart("image")MultipartFile image, @RequestPart("reference")MultipartFile reference) {
//        cookieService.createCookie(null, video, image, reference, null);
//        return ResponseEntity.ok().build();
//    }
}
