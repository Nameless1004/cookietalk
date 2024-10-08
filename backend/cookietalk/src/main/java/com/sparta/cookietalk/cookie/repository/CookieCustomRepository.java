package com.sparta.cookietalk.cookie.repository;

import com.sparta.cookietalk.common.dto.Response;
import com.sparta.cookietalk.cookie.dto.CookieResponse;
import com.sparta.cookietalk.cookie.dto.CookieResponse.Detail;
import com.sparta.cookietalk.cookie.dto.CookieSearch;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CookieCustomRepository {
    Detail getCookieDetails(Long id);
    Response.Page<CookieResponse.List> findCookieListByChannelId(Long channelId, Pageable pageable, boolean isMine);
    Response.Page<CookieResponse.List> searchCookieListByKeyword(Pageable pageable, CookieSearch search);

    Response.Slice<CookieResponse.List> getSliceByCategoryId(int size, LocalDateTime cursor, CookieSearch search);

    List<CookieResponse.RecentList> getRecentCookies(List<Long> longList);

    List<CookieResponse.SeriesList> getCookiesInSeries(long seriesId);
}
