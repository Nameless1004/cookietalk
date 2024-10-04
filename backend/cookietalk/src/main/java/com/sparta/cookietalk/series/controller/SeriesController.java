package com.sparta.cookietalk.series.controller;

import com.sparta.cookietalk.common.dto.ResponseDto;
import com.sparta.cookietalk.security.AuthUser;
import com.sparta.cookietalk.series.dto.SeriesRequest;
import com.sparta.cookietalk.series.dto.SeriesResponse;
import com.sparta.cookietalk.series.service.SeriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SeriesController {

    private final SeriesService seriesService;

    @PostMapping("/channels/{channelId}/series")
    public ResponseEntity<ResponseDto<SeriesResponse.Create>> registSeries(
        @PathVariable Long channelId,
        @AuthenticationPrincipal
        AuthUser authUser, @RequestBody SeriesRequest.Create request){
        SeriesResponse.Create series = seriesService.createSeries(authUser, channelId, request);
        return ResponseDto.toEntity(HttpStatus.CREATED, series);
    }

    @PatchMapping("/channels/{channelId}/series/{seriesId}")
    public ResponseEntity<ResponseDto<Void>> updateSeries(
        @AuthenticationPrincipal AuthUser authUser,
        @PathVariable("channelId") Long channelId,
        @PathVariable("seriesId") Long seriesId,
        @RequestBody  SeriesRequest.Patch request
    ) {
        seriesService.updateSeries(authUser, channelId, seriesId, request);
        return ResponseDto.toEntity(HttpStatus.OK, "변경에 성공하였습니다.", null);
    }

}
