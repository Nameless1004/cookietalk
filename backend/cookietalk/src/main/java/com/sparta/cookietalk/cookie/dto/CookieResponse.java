package com.sparta.cookietalk.cookie.dto;

import com.sparta.cookietalk.common.enums.ProcessStatus;
import com.sparta.cookietalk.cookie.dto.CookieResponse.Create;
import com.sparta.cookietalk.cookie.dto.CookieResponse.Detail;
import com.sparta.cookietalk.cookie.dto.CookieResponse.List;
import java.time.LocalDateTime;

public sealed interface CookieResponse permits Create, Detail, List {
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
        LocalDateTime createdAt) implements CookieResponse {}

    record List(
        Long userId,
        String userNickname,
        Long channelId,
        Long categoryId,
        String categoryName,
        Long cookieId,
        String title,
        String description,
        String thumbnailPath,
        ProcessStatus cookieStatus,
        LocalDateTime createdAt
    ) implements CookieResponse {}
}
