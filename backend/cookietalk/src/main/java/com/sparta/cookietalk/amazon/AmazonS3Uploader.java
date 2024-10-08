package com.sparta.cookietalk.amazon;

import com.sparta.cookietalk.common.enums.UploadType;
import com.sparta.cookietalk.common.utils.FileUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.NoSuchFileException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
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
    public Optional<S3UploadResponseDto> uploadVideoToS3(String prefixKey, File[] files) {
        String m3u8S3Key = "";
        File folder = files[0].getParentFile();

        try {
            for (File file : files) {
                uploadSingleFileToS3(prefixKey, file);

                if (file.getName()
                    .endsWith("m3u8")) {
                    m3u8S3Key = prefixKey + "/" + file.getName();
                }
            }
        } catch (Exception e) {
            handleS3UploadFailure(prefixKey, files);
            return Optional.empty();
        } finally {
            // 임시 파일 삭제
            deleteTemporaryFiles(folder);
        }

        log.info("업로드 완료");
        return Optional.of(S3UploadResponseDto.builder()
            .s3Url(cloudFrontDomain + "/" + m3u8S3Key)
            .s3Key(prefixKey)
            .build());
    }

    private void uploadSingleFileToS3(String prefixKey, File file) {
        String key = prefixKey + "/" + file.getName();
        PutObjectRequest req = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build();

        RequestBody requestBody = RequestBody.fromFile(file);
        s3Client.putObject(req, requestBody);
    }

    private void handleS3UploadFailure(String prefixKey, File[] files) {
        log.error("S3 업로드 중 오류 발생, 업로드된 파일 삭제 중...");
        for (File file : files) {
            try {
                deleteSingleFileFromS3(prefixKey, file.getName());
            } catch (Exception e) {
                log.error("S3에서 파일 삭제 실패: " + file.getName(), e);
            }
        }
    }

    private void deleteSingleFileFromS3(String prefixKey, String fileName) {
        String key = prefixKey + "/" + fileName;
        DeleteObjectRequest req = DeleteObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build();
        s3Client.deleteObject(req);
    }


    private void deleteTemporaryFiles(File folder) {
        try {
            fileUtils.deleteFileRecur(folder);
            log.info("임시 파일 삭제 완료");
        } catch (Exception e) {
            log.error("임시 파일 삭제 실패", e);
        }
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

    public Optional<S3UploadResponseDto> uploadToS3(String prefixKey, File file) {
        try{
            uploadSingleFileToS3(prefixKey, file);
            String key = prefixKey + "/" + file.getName();
            return Optional.of(S3UploadResponseDto.builder()
                    .s3Url(cloudFrontDomain + "/" + key)
                    .s3Key(key)
                .build());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<S3UploadResponseDto> uploadMultipartFileToS3(String prefixKey, MultipartFile file) {
        try{
            String key = prefixKey + "/" + file.getOriginalFilename();
            PutObjectRequest req = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

            RequestBody requestBody = RequestBody.fromInputStream(file.getInputStream(), file.getSize());
            s3Client.putObject(req, requestBody);
            return Optional.of(S3UploadResponseDto.builder()
                .s3Url(cloudFrontDomain + "/" + key)
                .s3Key(key)
                .build());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
