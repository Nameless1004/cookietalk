package com.sparta.cookietalk.cookie.repository;

import com.sparta.cookietalk.cookie.dto.CookieResponse;
import com.sparta.cookietalk.cookie.dto.CookieResponse.Detail;
import com.sparta.cookietalk.cookie.dto.KeywordSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CookieCustomRepository {
    Detail getCookieDetails(Long id);
    Page<CookieResponse.List> findCookieListByChannelId(Long channelId, Pageable pageable, boolean isMine);
    Page<CookieResponse.List> searchCookieList(Pageable pageable, KeywordSearch search);
}
