package com.sparta.cookietalk.channel.dto;

import com.sparta.cookietalk.channel.dto.ChannelRequest.Update;

public sealed interface ChannelRequest permits Update {
    record Update(
        String description,
        String businessEmail,
        String blogUrl,
        String githubUrl
    ) implements ChannelRequest {}
}
