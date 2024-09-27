package com.sparta.cookietalk.amazon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class S3UploadResponseDto {
    private String s3Key;
    private String s3Url;
}
