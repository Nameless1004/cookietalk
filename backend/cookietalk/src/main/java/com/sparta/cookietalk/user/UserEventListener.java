package com.sparta.cookietalk.user;

import com.sparta.cookietalk.channel.entity.Channel;
import com.sparta.cookietalk.channel.repository.ChannelRepository;
import com.sparta.cookietalk.user.entity.User;
import jakarta.persistence.PostPersist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class UserEventListener {
    @Lazy
    @Autowired
    private ChannelRepository channelRepository;

    @PostPersist
    public void postPersist(User user) {
        Channel channel = new Channel(user);
        channelRepository.save(channel);
    }
}
