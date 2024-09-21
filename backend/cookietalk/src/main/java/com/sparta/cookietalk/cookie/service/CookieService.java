package com.sparta.cookietalk.cookie.service;

import com.sparta.cookietalk.category.entity.Category;
import com.sparta.cookietalk.category.entity.CookieCategory;
import com.sparta.cookietalk.category.repository.CategoryRepository;
import com.sparta.cookietalk.category.repository.CookieCategoryRepository;
import com.sparta.cookietalk.channel.entity.Channel;
import com.sparta.cookietalk.channel.repository.ChannelRepository;
import com.sparta.cookietalk.common.enums.ProccessStatus;
import com.sparta.cookietalk.common.enums.UploadStatus;
import com.sparta.cookietalk.common.exceptions.AuthException;
import com.sparta.cookietalk.common.exceptions.FileUploadInProgressException;
import com.sparta.cookietalk.common.exceptions.InvalidRequestException;
import com.sparta.cookietalk.cookie.dto.CookieRequest.Create;
import com.sparta.cookietalk.cookie.dto.CookieResponse;
import com.sparta.cookietalk.cookie.dto.CookieResponse.List;
import com.sparta.cookietalk.cookie.entity.Cookie;
import com.sparta.cookietalk.cookie.repository.CookieRepository;
import com.sparta.cookietalk.series.entity.Series;
import com.sparta.cookietalk.series.entity.SeriesCookie;
import com.sparta.cookietalk.series.repository.SeriesCookieRepository;
import com.sparta.cookietalk.series.repository.SeriesRepository;
import com.sparta.cookietalk.upload.UploadFile;
import com.sparta.cookietalk.upload.UploadFileRepository;
import com.sparta.cookietalk.user.entity.User;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CookieService {

    private final CookieRepository cookieRepository;

    private final CategoryRepository categoryRepository;
    private final CookieCategoryRepository cookieCategoryRepository;
    private final SeriesRepository seriesRepository;
    private final SeriesCookieRepository seriesCookieRepository;

    private final ChannelRepository channelRepository;
    private final UploadFileRepository uploadFileRepository;

    public CookieResponse.Create createCookie(User auth, Long channelId, Create requestDto) {
        Channel channel = channelRepository.findChannelWithUserById(channelId)
            .orElseThrow(() -> new InvalidRequestException("존재하지 않는 채널입니다."));

        Category category = categoryRepository.findById(requestDto.cartegoryId())
            .orElseThrow(() -> new InvalidRequestException("존재하지 않는 카테고리 입니다."));

        if(channel.getUser() != auth){
            throw new AuthException("채널 주인이 아닙니다.");
        }

        Series series = null;
        if(requestDto.seriesId() != null) {
            series = seriesRepository.findById(requestDto.seriesId()).orElseThrow(()-> new InvalidRequestException("존재하지 않는 시리즈입니다."));

            if(Objects.equals(series.getChannel().getId(), channelId)) {
                throw new InvalidRequestException("해당 채널에 해당 시리즈가 존재하지 않습니다.");
            }
        }

        UploadFile video = uploadFileRepository.findByIdOrElseThorw(requestDto.videoFileId());
        UploadFile thumbnail = uploadFileRepository.findByIdOrElseThorw(requestDto.thumbnailFileId());
        UploadFile attachment = requestDto.attachmentFileId() == null ? null : uploadFileRepository.findByIdOrElseThorw(requestDto.attachmentFileId());

        fileUploadCompleteCheck(video, thumbnail, attachment);
        Cookie newCookie = Cookie.builder()
            .channel(channel)
            .videoFile(video)
            .thumbnailFile(thumbnail)
            .attachmentFile(attachment)
            .status(ProccessStatus.SUCCESS)
            .title(requestDto.title())
            .description(requestDto.description())
            .build();
        newCookie = cookieRepository.save(newCookie);

        // 시리즈 연결
        if(series != null){
            SeriesCookie newSeriesCookie = new SeriesCookie(series, newCookie);
            seriesCookieRepository.save(newSeriesCookie);
        }

        // 카테고리 데이터 저장
        CookieCategory cookieCategory = new CookieCategory(newCookie, category);
        cookieCategoryRepository.save(cookieCategory);


        return new CookieResponse.Create(newCookie.getId());
    }

    private void fileUploadCompleteCheck(UploadFile... video) {
        for (UploadFile file : video) {
            if(file == null) continue;
            if(file.getStatus() != UploadStatus.COMPLETED) {
                throw new FileUploadInProgressException();
            }
        }
    }

    public CookieResponse.Detail getCookie(Long cookieId) {
        if(!cookieRepository.existsById(cookieId)) {
            throw new InvalidRequestException("존재하지 않는 쿠키입니다.");
        }

        return cookieRepository.getCookieDetails(cookieId);
    }
}
