package com.sparta.cookietalk.cookie.dto;

import com.sparta.cookietalk.common.enums.ProcessStatus;
import com.sparta.cookietalk.cookie.dto.CookieResponse.Create;
import com.sparta.cookietalk.cookie.dto.CookieResponse.Detail;
import com.sparta.cookietalk.cookie.dto.CookieResponse.List;
import com.sparta.cookietalk.cookie.dto.CookieResponse.RecentList;
import com.sparta.cookietalk.cookie.dto.CookieResponse.SeriesList;
import java.time.LocalDateTime;

public sealed interface CookieResponse permits Create, Detail, List, RecentList, SeriesList {
    record Create(
        Long cookieId
    ) implements CookieResponse {}

    record Detail(
        Long channelId,
        Long userId,
        String userNickname,
        Long cookieId,
        ProcessStatus cookieStatus,
        String title,
        String description,
        Long uploadFileId,
        Long thumbnailFileId,
        Long attachmentFileId,
        Long seriesId,
        LocalDateTime createdAt) implements CookieResponse {}

    record List(
        Long userId,
        String userNickname,
        Long cookieId,
        String title,
        String thumbnailPath,
        ProcessStatus cookieStatus,
        LocalDateTime createdAt
    ) implements CookieResponse {}

    record SeriesList(
        Long userId,
        String userNickname,
        Long cookieId,
        String title,
        String thumbnailPath,
        ProcessStatus cookieStatus,
        LocalDateTime addedAt
    ) implements CookieResponse {}

    record RecentList(
        Long userId,
        String userNickname,
        Long cookieId,
        String title,
        String thumbnailPath,
        LocalDateTime viewAt
    ) implements CookieResponse {}
}
