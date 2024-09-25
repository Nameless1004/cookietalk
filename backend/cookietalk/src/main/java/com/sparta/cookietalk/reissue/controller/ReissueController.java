package com.sparta.cookietalk.reissue.controller;

import com.sparta.cookietalk.common.dto.ResponseDto;
import com.sparta.cookietalk.reissue.service.ReissueService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReissueController {

    private final ReissueService reissueService;

    @PostMapping("/api/user/reissue")
    public ResponseEntity<ResponseDto<Void>> reissue(HttpServletRequest request, HttpServletResponse response) {
        ResponseDto<Void> reissue = reissueService.reissue(request, response);

        return ResponseDto.toEntity(reissue);
    }

}