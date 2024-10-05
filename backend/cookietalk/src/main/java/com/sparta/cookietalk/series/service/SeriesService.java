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
import com.sparta.cookietalk.user.entity.User;
import com.sparta.cookietalk.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SeriesService {

    private final ChannelRepository channelRepository;
    private final SeriesRepository seriesRepository;
    private final UserRepository userRepository;

    public SeriesResponse.Create createSeries(AuthUser user, Create request) {
        Channel channel = channelRepository.findChannelWithUserByUserId(user.getUserId())
            .orElseThrow(() -> new InvalidRequestException("존재하지 않는 채널입니다."));

        if(channel.getUser().getId() != user.getUserId()){
            throw new AuthException("생성 권한이 없습니다.");
        }

        Series series = new Series(channel, request.title());
        series = seriesRepository.save(series);
        return new SeriesResponse.Create(series.getId(), series.getTitle());
    }

    public void updateSeries(AuthUser user, Long seriesId, Patch request) {
        Series series = seriesRepository.findWithChannelAndUserById(seriesId)
            .orElseThrow(() -> new InvalidRequestException("존재하지 않는 시리즈입니다."));

        if(series.getChannel().getUser().getId() != user.getUserId()){
            throw new AuthException("수정 권한이 없습니다.");
        }

        series.updateTitle(request.title());
    }

    public void deleteSeries(AuthUser authUser, Long seriesId) {
        Series series = seriesRepository.findWithChannelAndUserById(seriesId)
            .orElseThrow(() -> new InvalidRequestException("존재하지 않는 시리즈입니다."));

        if(series.getChannel().getUser().getId() != authUser.getUserId()){
            throw new AuthException("삭제 권한이 없습니다.");
        }

        seriesRepository.delete(series);
    }

    public List<SeriesResponse.List> getUserSeriesById(Long userId) {
        User user = userRepository.findByIdOrElseThrow(userId);
        List<Series> result = seriesRepository.findAllByUser(user);
        return result.stream().map(SeriesResponse.List::new).toList();
    }
}
