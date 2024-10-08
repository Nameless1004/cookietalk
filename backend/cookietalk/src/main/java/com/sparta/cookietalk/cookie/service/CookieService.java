package com.sparta.cookietalk.cookie.service;

import com.sparta.cookietalk.channel.entity.Channel;
import com.sparta.cookietalk.channel.repository.ChannelRepository;
import com.sparta.cookietalk.common.defines.Define;
import com.sparta.cookietalk.common.dto.Response;
import com.sparta.cookietalk.common.enums.ProcessStatus;
import com.sparta.cookietalk.common.enums.UploadStatus;
import com.sparta.cookietalk.common.exceptions.InvalidRequestException;
import com.sparta.cookietalk.cookie.dto.CookieRequest.Create;
import com.sparta.cookietalk.cookie.dto.CookieResponse;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.sparta.cookietalk.cookie.dto.CookieSearch;
import com.sparta.cookietalk.cookie.entity.Cookie;
import com.sparta.cookietalk.cookie.entity.UserRecentCookie;
import com.sparta.cookietalk.cookie.repository.CookieRepository;
import com.sparta.cookietalk.cookie.repository.UserRecentCookieRepository;
import com.sparta.cookietalk.security.AuthUser;
import com.sparta.cookietalk.upload.UploadFile;
import com.sparta.cookietalk.user.entity.User;
import com.sparta.cookietalk.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CookieService {

    private final CookieRepository cookieRepository;
    private final ChannelRepository channelRepository;
    private final CookieCreateFacade cookieCreateFacade;
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserRecentCookieRepository userRecentCookieRepository;
    private final UserRepository userRepository;

    /**
     * 쿠키 생성
     * @param auth
     * @param requestDto
     * @param video
     * @param thumbnail
     * @param attachment
     * @return
     */
    public CookieResponse.Create createCookie(AuthUser auth, Create requestDto,
        MultipartFile video,
        MultipartFile thumbnail,
        MultipartFile attachment) {
        return cookieCreateFacade.createCookie(auth, requestDto, video, thumbnail, attachment);
    }

    /**
     * 쿠키 상세 조회
     *
     * @param authUser
     * @param cookieId
     * @return
     */
    public CookieResponse.Detail getCookie(AuthUser authUser, Long cookieId) {
        Cookie cookie = cookieRepository.findById(cookieId)
            .orElseThrow(() -> new InvalidRequestException("존재하지 않는 쿠키입니다."));
        
        User user = userRepository.findByIdOrElseThrow(authUser.getUserId());
        
        // 조회 수 증가
        cookie.incrementView();

        // 시청 기록 저장
        saveRecentCookiesToDB(user, cookie);

        cookieRepository.save(cookie);

        return cookieRepository.getCookieDetails(cookie.getId());
    }

    /**
     * 레디스와 MySQL에 시청 기록 저장
     * @param user
     * @param cookie
     */
    private void saveRecentCookiesToDB(User user, Cookie cookie) {
        String key = Define.REDIS_RECENT_COOKIE_PREFIX + user.getId();

        redisTemplate.opsForList().remove(key, 0, cookie.getId());
        redisTemplate.opsForList().leftPush(key, cookie.getId());
        redisTemplate.opsForList().trim(key, 0, 9);

        Optional<UserRecentCookie> urc = userRecentCookieRepository.findByCookieId(
            cookie.getId());

        if(urc.isPresent()) {
            UserRecentCookie userRecentCookie = urc.get();
            userRecentCookie.updateViewAt();
        } else  {
            userRecentCookieRepository.save(new UserRecentCookie(user, cookie));
        }

    }

    @Transactional(readOnly = true)
    public Response.Page<CookieResponse.List> getCookieListByUserId(AuthUser auth, Long userId, int page, int size) {
        Channel channel = channelRepository.findChannelWithUserByUserId(userId)
            .orElseThrow(() -> new InvalidRequestException("존재하지 않는 채널입니다."));

        Pageable pageable = PageRequest.of(page - 1, size);
        if(auth.getUserId() == userId) {
            return cookieRepository.findCookieListByChannelId(channel.getId(), pageable, true);
        } else {
            return cookieRepository.findCookieListByChannelId(channel.getId(), pageable, false);
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
    @Transactional(readOnly = true)
    public Response.Page<CookieResponse.List> searchKeyword(int page, int size,
        CookieSearch search) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return cookieRepository.searchCookieListByKeyword(pageable, search);
    }

    /**
     * 무한 스크롤 방식
     * @return
     */
    @Transactional(readOnly = true)
    public Response.Slice<CookieResponse.List> getCookieListByCategory(int size, LocalDateTime startDateTime, CookieSearch search) {
        return cookieRepository.getSliceByCategoryId(size, startDateTime, search);
    }

    public List<CookieResponse.RecentList> getRecentCookies(long userId) {
        String key = Define.REDIS_RECENT_COOKIE_PREFIX + userId;
        List<Long> values = redisTemplate.opsForList().range(key, 0, -1).stream()
            .map(x-> Long.valueOf(String.valueOf(x)))  // Object를 String으로 변환 후 Long으로 변환
            .collect(Collectors.toList());

        return cookieRepository.getRecentCookies(values);
    }
}
