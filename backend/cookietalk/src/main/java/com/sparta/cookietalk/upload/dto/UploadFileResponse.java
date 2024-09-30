package com.sparta.cookietalk.upload.dto;

import com.sparta.cookietalk.upload.dto.UploadFileResponse.Detail;

public sealed interface UploadFileResponse permits Detail {
    record Detail(Long id, String s3Url) implements UploadFileResponse { }
}
