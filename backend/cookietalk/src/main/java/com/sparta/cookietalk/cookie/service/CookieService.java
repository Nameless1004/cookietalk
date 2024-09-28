package com.sparta.cookietalk.cookie.service;

import com.sparta.cookietalk.category.repository.CategoryRepository;
import com.sparta.cookietalk.category.repository.CookieCategoryRepository;
import com.sparta.cookietalk.channel.entity.Channel;
import com.sparta.cookietalk.channel.repository.ChannelRepository;
import com.sparta.cookietalk.common.enums.UploadStatus;
import com.sparta.cookietalk.common.exceptions.FileUploadInProgressException;
import com.sparta.cookietalk.common.exceptions.InvalidRequestException;
import com.sparta.cookietalk.cookie.dto.CookieRequest.Create;
import com.sparta.cookietalk.cookie.dto.CookieResponse;
import com.sparta.cookietalk.cookie.dto.CookieResponse.Detail;
import com.sparta.cookietalk.cookie.repository.CookieRepository;
import com.sparta.cookietalk.series.repository.SeriesCookieRepository;
import com.sparta.cookietalk.series.repository.SeriesRepository;
import com.sparta.cookietalk.upload.UploadFile;
import com.sparta.cookietalk.upload.UploadFileRepository;
import com.sparta.cookietalk.user.entity.User;
import com.sparta.cookietalk.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

    private final CookieCreateFacade cookieCreateFacade;
    private final UserRepository userRepository;

    public CookieResponse.Create createCookie(User auth, Create requestDto,
        MultipartFile video,
        MultipartFile thumbnail,
        MultipartFile attachment) {
        return cookieCreateFacade.createCookie(auth, requestDto, video, thumbnail, attachment);
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

    /**
     * 사용자와 요청한 userId가 같다면 PENDING상태도 보여줍니다.
     *
     */
    public Page<CookieResponse.Detail> getCookiesByChannelId(User auth, Long channelId, int page, int size) {
        Channel channel = channelRepository.findChannelWithUserById(channelId)
            .orElseThrow(() -> new InvalidRequestException("존재하지 않는 채널입니다."));

        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Detail> allCookiesByChannelId = cookieRepository.findAllCookiesByChannelId(
            channel.getId(), pageable);
        //if(auth.equals(channel.getUser())) {
            // todo: dafsd

        return allCookiesByChannelId;
    }
}
