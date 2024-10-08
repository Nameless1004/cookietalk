package com.sparta.cookietalk.upload;

import com.sparta.cookietalk.amazon.S3UploadResponseDto;
import com.sparta.cookietalk.common.entity.Timestamped;
import com.sparta.cookietalk.common.enums.UploadStatus;
import com.sparta.cookietalk.common.enums.UploadType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UploadFile extends Timestamped {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private UploadType uploadType;

    @Enumerated(EnumType.STRING) // 업로드 중, 업로드 완료
    private UploadStatus status;

    private String s3Key;
    private String s3Url;

    @Builder
    public UploadFile(UploadType uploadType, UploadStatus status, String s3Key, String s3Url) {
        this.uploadType = uploadType;
        this.status = status;
        this.s3Key = s3Key;
        this.s3Url = s3Url;
    }

    public void registS3Url(String s3Url) {
        this.s3Url = s3Url;
    }

    public void registS3Key(String key) {
        this.s3Key = key;
    }

    public void updateStatus(UploadStatus uploadStatus) {
        this.status = uploadStatus;
    }

    public void updateS3(S3UploadResponseDto res) {
        this.s3Key = res.getS3Key();
        this.s3Url = res.getS3Url();
    }

}
