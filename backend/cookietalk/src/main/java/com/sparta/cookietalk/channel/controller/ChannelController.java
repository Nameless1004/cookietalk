package com.sparta.cookietalk.channel.controller;

import com.sparta.cookietalk.channel.dto.ChannelRequest;
import com.sparta.cookietalk.channel.dto.ChannelRequest.Update;
import com.sparta.cookietalk.channel.dto.ChannelResponse;
import com.sparta.cookietalk.channel.service.ChannelService;
import com.sparta.cookietalk.common.dto.ResponseDto;
import com.sparta.cookietalk.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @GetMapping("/api/v1/users/{userId}/channels/profile")
    public ResponseEntity<ResponseDto<ChannelResponse.Profile>> getChannelProfile(
        @PathVariable("userId") long userId
    ) {
        return ResponseDto.toEntity(HttpStatus.OK, channelService.getChannelProfile(userId));
    }

    @PatchMapping("/api/v1/users/{userId}/channels/profile")
    public ResponseEntity<ResponseDto<ChannelResponse.Profile>> updateChannel(
        @PathVariable("userId") long userId,
        @RequestPart("profile") MultipartFile profile,
        @RequestPart("update") Update update,
        @AuthenticationPrincipal AuthUser authUser) {

        return ResponseDto.toEntity(HttpStatus.OK, channelService.updateProfile(userId, profile, update, authUser));
    }
}
