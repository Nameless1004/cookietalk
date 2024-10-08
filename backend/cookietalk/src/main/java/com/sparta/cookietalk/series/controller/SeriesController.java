package com.sparta.cookietalk.series.controller;

import com.sparta.cookietalk.common.dto.ResponseDto;
import com.sparta.cookietalk.security.AuthUser;
import com.sparta.cookietalk.series.dto.SeriesRequest;
import com.sparta.cookietalk.series.dto.SeriesResponse;
import com.sparta.cookietalk.series.service.SeriesService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    /**
     * 시리즈 생성
     * @param authUser
     * @param request
     * @return
     */
    @PostMapping("v1/series")
    public ResponseEntity<ResponseDto<SeriesResponse.Create>> registSeries(
        @AuthenticationPrincipal AuthUser authUser,
        @RequestBody SeriesRequest.Create request){

        SeriesResponse.Create series = seriesService.createSeries(authUser, request);
        return ResponseDto.toEntity(HttpStatus.CREATED, series);
    }

    /**
     * 시리즈 업데이트
     * @param authUser
     * @param seriesId
     * @param request
     * @return
     */
    @PatchMapping("v1/series/{seriesId}")
    public ResponseEntity<ResponseDto<Void>> updateSeries(
        @AuthenticationPrincipal AuthUser authUser,
        @PathVariable("seriesId") Long seriesId,
        @RequestBody  SeriesRequest.Patch request) {

        seriesService.updateSeries(authUser, seriesId, request);
        return ResponseDto.toEntity(HttpStatus.OK, "변경에 성공하였습니다.", null);
    }

    /**
     * 시리즈 삭제
     * @param authUser
     * @param seriesId
     * @return
     */
    @DeleteMapping("v1/series/{seriesId}")
    public ResponseEntity<ResponseDto<Void>> deleteSeries(
        @AuthenticationPrincipal AuthUser authUser,
        @PathVariable("seriesId") Long seriesId) {
        seriesService.deleteSeries(authUser, seriesId);
        return ResponseDto.toEntity(HttpStatus.OK, "성공적으로 삭제되었습니다.", null);
    }

    /**
     * 해당 유저의 시리즈 목록 가져오기
     * @param userId
     * @return
     */
    @GetMapping("/v1/users/{userId}/series")
    public ResponseEntity<ResponseDto<List<SeriesResponse.List>>> getUserSeriesList(
        @PathVariable("userId") Long userId
    ) {
        return ResponseDto.toEntity(HttpStatus.OK, seriesService.getUserSeriesById(userId));
    }
}
