package com.sparta.cookietalk.upload;

import com.sparta.cookietalk.common.enums.UploadStatus;

public interface UploadFileCutomRepository {
    UploadStatus findStatusById(long id);
}
