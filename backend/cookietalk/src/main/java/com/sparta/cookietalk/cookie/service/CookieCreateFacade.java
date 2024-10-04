package com.sparta.cookietalk.cookie.service;

import com.sparta.cookietalk.category.entity.Category;
import com.sparta.cookietalk.category.entity.CookieCategory;
import com.sparta.cookietalk.category.repository.CategoryRepository;
import com.sparta.cookietalk.category.repository.CookieCategoryRepository;
import com.sparta.cookietalk.channel.entity.Channel;
import com.sparta.cookietalk.channel.repository.ChannelRepository;
import com.sparta.cookietalk.common.enums.ProcessStatus;
import com.sparta.cookietalk.common.enums.UploadType;
import com.sparta.cookietalk.common.exceptions.InvalidRequestException;
import com.sparta.cookietalk.cookie.dto.CookieRequest;
import com.sparta.cookietalk.cookie.dto.CookieResponse;
import com.sparta.cookietalk.cookie.dto.CookieResponse.Create;
import com.sparta.cookietalk.cookie.entity.Cookie;
import com.sparta.cookietalk.cookie.repository.CookieRepository;
import com.sparta.cookietalk.security.AuthUser;
import com.sparta.cookietalk.series.entity.Series;
import com.sparta.cookietalk.series.entity.SeriesCookie;
import com.sparta.cookietalk.series.repository.SeriesCookieRepository;
import com.sparta.cookietalk.series.repository.SeriesRepository;
import com.sparta.cookietalk.upload.UploadFile;
import com.sparta.cookietalk.upload.UploadService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
@Transactional
public class CookieCreateFacade {
    private final CookieRepository cookieRepository;
    private final ChannelRepository channelRepository;
    private final CategoryRepository categoryRepository;
    private final SeriesRepository seriesRepository;
    private final UploadService uploadService;
    private final SeriesCookieRepository seriesCookieRepository;
    private final CookieCategoryRepository cookieCategoryRepository;

    public CookieResponse.Create createCookie(
        AuthUser user,
        CookieRequest.Create createDto,
        MultipartFile video,
        MultipartFile thumbnail,
        MultipartFile attachment) {

        Channel channel = channelRepository.findChannelWithUserByUserId(user.getUserId())
            .orElseThrow(() -> new InvalidRequestException("유저의 채널이 존재하지 않습니다."));

        Category category = categoryRepository.findById(createDto.categoryId())
            .orElseThrow(() -> new InvalidRequestException("존재하지 않는 카테고리 입니다."));

        Series series = null;
        if(createDto.seriesId() != null) {
            series = seriesRepository.findById(createDto.seriesId()).orElseThrow(()-> new InvalidRequestException("존재하지 않는 시리즈입니다."));

            if(Objects.equals(series.getChannel().getId(), channel.getId())) {
                throw new InvalidRequestException("해당 채널에 해당 시리즈가 존재하지 않습니다.");
            }
        }

        UploadFile uploadedVideo = uploadService.uploadVideo(video);
        UploadFile uploadedThumbnail = uploadService.uploadFile(UploadType.IMAGE, thumbnail);
        UploadFile uploadedAttachment = attachment == null ? null : uploadService.uploadFile(UploadType.ATTACHMENT, attachment);

        Cookie newCookie = createNewCookie(createDto, channel, uploadedVideo, uploadedThumbnail,
            uploadedAttachment);

        newCookie = cookieRepository.save(newCookie);

        // 시리즈 연결
        if(series != null) {
            SeriesCookie seriesCookie = new SeriesCookie(series, newCookie);
            seriesCookieRepository.save(seriesCookie);
        }

        // 카테고리 연결
        CookieCategory cc = new CookieCategory(newCookie, category);
        cookieCategoryRepository.save(cc);

        return new Create(newCookie.getId());
    }

    public Cookie createNewCookie(  CookieRequest.Create createDto, Channel channel, UploadFile video, UploadFile thumbnail, UploadFile attachment) {
        return Cookie.builder()
            .channel(channel)
            .title(createDto.title())
            .description(createDto.description())
            .status(ProcessStatus.PENDING)
            .videoFile(video)
            .thumbnailFile(thumbnail)
            .attachmentFile(attachment)
            .build();
    }
}
