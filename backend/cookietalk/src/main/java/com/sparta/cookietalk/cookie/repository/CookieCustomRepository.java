package com.sparta.cookietalk.cookie.repository;

import com.sparta.cookietalk.common.dto.Response;
import com.sparta.cookietalk.cookie.dto.CookieResponse;
import com.sparta.cookietalk.cookie.dto.CookieResponse.Detail;
import com.sparta.cookietalk.cookie.dto.CookieResponse.List;
import com.sparta.cookietalk.cookie.dto.CookieSearch;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CookieCustomRepository {
    Detail getCookieDetails(Long id);
    Page<CookieResponse.List> findCookieListByChannelId(Long channelId, Pageable pageable, boolean isMine);
    Page<CookieResponse.List> searchCookieListByKeyword(Pageable pageable, CookieSearch search);

    Response.Slice<CookieResponse.List> getSliceByCategoryId(int size, LocalDateTime startDateTime, CookieSearch search);
}
