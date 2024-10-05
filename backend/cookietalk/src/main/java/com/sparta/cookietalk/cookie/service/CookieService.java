package com.sparta.cookietalk.cookie.service;

import com.sparta.cookietalk.category.repository.CategoryRepository;
import com.sparta.cookietalk.category.repository.CookieCategoryRepository;
import com.sparta.cookietalk.channel.entity.Channel;
import com.sparta.cookietalk.channel.repository.ChannelRepository;
import com.sparta.cookietalk.common.dto.Response;
import com.sparta.cookietalk.common.dto.Response.Page;
import com.sparta.cookietalk.common.enums.ProcessStatus;
import com.sparta.cookietalk.common.enums.UploadStatus;
import com.sparta.cookietalk.common.exceptions.FileUploadInProgressException;
import com.sparta.cookietalk.common.exceptions.InvalidRequestException;
import com.sparta.cookietalk.cookie.dto.CategorySearch;
import com.sparta.cookietalk.cookie.dto.CookieRequest.Create;
import com.sparta.cookietalk.cookie.dto.CookieResponse;
import com.sparta.cookietalk.cookie.dto.CookieResponse.List;
import com.sparta.cookietalk.cookie.dto.CookieSearch;
import com.sparta.cookietalk.cookie.entity.Cookie;
import com.sparta.cookietalk.cookie.repository.CookieRepository;
import com.sparta.cookietalk.security.AuthUser;
import com.sparta.cookietalk.series.repository.SeriesCookieRepository;
import com.sparta.cookietalk.series.repository.SeriesRepository;
import com.sparta.cookietalk.upload.UploadFile;
import com.sparta.cookietalk.upload.UploadFileRepository;
import com.sparta.cookietalk.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public CookieResponse.Create createCookie(AuthUser auth, Create requestDto,
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

    public Response.Page<CookieResponse.List> getCookieListByUserId(AuthUser auth, Long userId, int page, int size) {
        Channel channel = channelRepository.findChannelWithUserByUserId(userId)
            .orElseThrow(() -> new InvalidRequestException("존재하지 않는 채널입니다."));

        Pageable pageable = PageRequest.of(page - 1, size);
        if(auth.getUserId() == userId) {
            return new Page<>(cookieRepository.findCookieListByChannelId(channel.getId(), pageable, true));
        } else {
            return new Page<>(cookieRepository.findCookieListByChannelId(channel.getId(), pageable, false));
        }
    }

    public void onFileUploadStatusChanged(Long changedUploadFileId) {
        Cookie cookie = cookieRepository.findByUploadFileId(changedUploadFileId);
        UploadFile thumbnailFile = cookie.getThumbnailFile();
        UploadFile videoFile = cookie.getVideoFile();
        UploadFile attachmentFile = cookie.getAttachmentFile();

        ArrayList<UploadFile> list = new ArrayList<>();
        list.add(thumbnailFile);
        list.add(videoFile);
        log.info("비디오 상태: {}", videoFile.getStatus().name());
        log.info("썸네일 상태: {}", thumbnailFile.getStatus().name());

        if(attachmentFile != null) {
            list.add(attachmentFile);
        }

        if(list.stream().anyMatch(x -> x.getStatus() == UploadStatus.FAILED)) {
            cookie.updateProcessStatus(ProcessStatus.FAILED);
            return;
        }

        if(list.stream().allMatch(x -> x.getStatus() == UploadStatus.COMPLETED)) {
            cookie.updateProcessStatus(ProcessStatus.SUCCESS);
        }
    }

    /**
     * 키워드 검색 조회
     * @param page
     * @param size
     * @return
     */
    public Response.Page<List> searchKeyword(int page, int size,
        CookieSearch search) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return new Response.Page<>(cookieRepository.searchCookieListByKeyword(pageable, search));
    }

    /**
     * 무한 스크롤 방식
     * @return
     */
    public Response.Slice<List> getCookieListByCategory(int size, LocalDateTime startDateTime, CookieSearch search) {
        return cookieRepository.getSliceByCategoryId(size, startDateTime, search);
    }
}
