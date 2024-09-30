package com.sparta.cookietalk.cookie.dto;

import com.sparta.cookietalk.cookie.dto.CookieRequest.Create;

public sealed interface CookieRequest permits Create {

    record Create(
        String title,
        String description,
        Long categoryId,
        Long seriesId
        ) implements CookieRequest {}
}
