package com.sparta.cookietalk.series.service;

import com.sparta.cookietalk.channel.entity.Channel;
import com.sparta.cookietalk.channel.repository.ChannelRepository;
import com.sparta.cookietalk.common.exceptions.AuthException;
import com.sparta.cookietalk.common.exceptions.InvalidRequestException;
import com.sparta.cookietalk.security.AuthUser;
import com.sparta.cookietalk.series.dto.SeriesRequest.Create;
import com.sparta.cookietalk.series.dto.SeriesRequest.Patch;
import com.sparta.cookietalk.series.dto.SeriesResponse;
import com.sparta.cookietalk.series.entity.Series;
import com.sparta.cookietalk.series.repository.SeriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SeriesService {

    private final ChannelRepository channelRepository;
    private final SeriesRepository seriesRepository;

    public SeriesResponse.Create createSeries(AuthUser user, Long channelId, Create request) {
        Channel channel = channelRepository.findChannelWithUserById(channelId)
            .orElseThrow(() -> new InvalidRequestException("존재하지 않는 채널입니다."));

        if(channel.getUser().getId() != user.getUserId()){
            throw new AuthException("채널 주인이 아닙니다.");
        }

        Series series = new Series(channel, request.title());
        series = seriesRepository.save(series);
        return new SeriesResponse.Create(series.getId(), series.getTitle());
    }

    public void updateSeries(AuthUser user, Long channelId, Long seriesId, Patch request) {
        Channel channel = channelRepository.findChannelWithUserById(channelId)
            .orElseThrow(() -> new InvalidRequestException("존재하지 않는 채널입니다."));

        if(channel.getUser().getId() != user.getUserId()){
            throw new AuthException("채널 주인이 아닙니다.");
        }

        Series series = seriesRepository.findById(seriesId)
            .orElseThrow(() -> new InvalidRequestException("존재하지 않는 시리즈입니다."));
        series.updateTitle(request.title());
    }
}
