package com.sparta.cookietalk.cookie.dto;

import com.sparta.cookietalk.cookie.dto.CookieResponse.Create;
import com.sparta.cookietalk.cookie.dto.CookieResponse.Detail;
import com.sparta.cookietalk.cookie.dto.CookieResponse.List;
import com.sparta.cookietalk.cookie.dto.CookieResponse.Detail;

public sealed interface CookieResponse permits Create, Detail, List {
    record Create(Long cookieId) implements CookieResponse {}
    record Detail(
        Long channelId,
        Long userId,
        String userNickname,
        Long cookieId,
        String title,
        String description,
        Long uploadFileId,
        Long thumbnailFileId,
        Long attachmentFileId) implements CookieResponse {}

    record List(
        Long cookieId,
        String title,
        String description,
        String thumbnailPath
    ) implements CookieResponse {}
}
