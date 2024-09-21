package com.sparta.cookietalk.cookie.dto;

import com.sparta.cookietalk.cookie.dto.CookieRequest.Create;

public sealed interface CookieRequest permits Create {

    record Create(
        String title,
        String description,
        Long cartegoryId,
        Long videoFileId,
        Long thumbnailFileId,
        Long attachmentFileId
        ) implements CookieRequest {}
}
