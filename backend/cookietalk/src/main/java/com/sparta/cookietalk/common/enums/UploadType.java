package com.sparta.cookietalk.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UploadType {
    VIDEO("videos",  "C:/temp/videos"),
    IMAGE("images",  "C:/temp/images"),
    REFERENCE("references",  "C:/temp/references");

    private final String key;
    private final String localSavePath;
}
