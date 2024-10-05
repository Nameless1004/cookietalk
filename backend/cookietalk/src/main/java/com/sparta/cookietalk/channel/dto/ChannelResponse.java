package com.sparta.cookietalk.channel.dto;

import com.sparta.cookietalk.channel.dto.ChannelResponse.Profile;
import lombok.Builder;

public sealed interface ChannelResponse permits Profile {
    @Builder
    record Profile(Long channelId,
        Long userId,
        String nickname,
        String description,
        String profileImageUrl,
        String githubUrl,
        String blogUrl,
        String businessEmail) implements ChannelResponse {}
}
