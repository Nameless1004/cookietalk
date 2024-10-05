package com.sparta.cookietalk.cookie.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CookieSearch {
    private Long categoryId;
    private String keyword;
}
