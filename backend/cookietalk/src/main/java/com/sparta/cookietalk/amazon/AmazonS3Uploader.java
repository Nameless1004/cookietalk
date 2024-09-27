package com.sparta.cookietalk.amazon;

import com.sparta.cookietalk.common.enums.UploadType;
import com.sparta.cookietalk.common.utils.FileUtils;
import java.io.File;
import java.nio.file.NoSuchFileException;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j(topic = "Amazon S3 Uploader")
@Component
@RequiredArgsConstructor
public class AmazonS3Uploader {

    private final FileUtils fileUtils;
    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.cloudfront.domain}")
    private String cloudFrontDomain;

    /**
     * @param prefixKey
     * @param files
     * @return
     */
    @Async
    public CompletableFuture<S3UploadResponseDto> uploadVideoToS3(String prefixKey, File[] files) {
        String m3u8Key = "";

        File folder = files[0].getParentFile();

        try {
            for (File file : files) {
                try {
                    // s3에 업로드
                    String key = prefixKey + "/" + file.getName();
                    PutObjectRequest req = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build();

                    RequestBody requestBody = RequestBody.fromFile(file);
                    s3Client.putObject(req, requestBody);

                    if (file.getName()
                        .endsWith("m3u8")) {
                        m3u8Key = key;
                    }
                } catch (AwsServiceException | SdkClientException e) {
                    throw new IllegalStateException("S3 업로드 실패");
                }
            }
        } finally {
            // 임시 파일 삭제
            fileUtils.deleteFileRecur(folder);
        }

        log.info("업로드 완료");
        return CompletableFuture.completedFuture(S3UploadResponseDto.builder()
            .s3Url(cloudFrontDomain + "/" + m3u8Key)
            .s3Key(prefixKey)
            .build());
    }

    @Async
    public CompletableFuture<S3UploadResponseDto> uploadFileToS3(String prefixKey, File file) {
        try {
            String key = prefixKey + "/" + file.getName();
            // s3에 업로드
            PutObjectRequest req = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

            RequestBody requestBody = RequestBody.fromFile(file);
            s3Client.putObject(req, requestBody);
        } catch (AwsServiceException | SdkClientException e) {
            log.error("File upload failed! file: {} / message: {}", file.getAbsolutePath(),
                e.getMessage());
        } finally {
            fileUtils.deleteFileRecur(file);
        }

        return CompletableFuture.completedFuture(
            S3UploadResponseDto.builder()
                .s3Key(prefixKey + "/" + file.getName())
                .s3Url(cloudFrontDomain + "/" + prefixKey + "/" + file.getName())
                .build());
    }

    /**
     * S3에 있는 파일을 삭제합니다. 비디오의 경우 key를 접두사로 사용해서 폴더안에있는 오브젝트들을 전부 삭제합니다.
     * @param uploadType
     * @param key
     */
    @Async
    public void deleteFile(UploadType uploadType, String key) {
        if(uploadType == UploadType.VIDEO) {
            ListObjectsV2Request build = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(key + "/")
                .build();

            ListObjectsV2Response response = s3Client.listObjectsV2(build);
            response .contents().forEach(s3Object -> {
                String objectKey = s3Object.key();
                s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build());
            });
        } else {
            DeleteObjectRequest req = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
            s3Client.deleteObject(req);
        }
    }
}
