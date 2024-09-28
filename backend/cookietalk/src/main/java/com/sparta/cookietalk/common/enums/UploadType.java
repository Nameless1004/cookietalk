package com.sparta.cookietalk.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UploadType {
    VIDEO("videos"),
    IMAGE("images"),
    ATTACHMENT("attachments");
    private final String key;
}
