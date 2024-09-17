package com.sparta.cookietalk.cookie.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
public class CookieCreateRequestDto {
    private String title;
    private String description;
    private Long videoFileId;
    private Long thumbnailFileId;
    private Long referenceFileId;
}
