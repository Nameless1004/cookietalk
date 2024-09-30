package com.sparta.cookietalk.upload;

import com.sparta.cookietalk.amazon.AmazonS3Uploader;
import com.sparta.cookietalk.amazon.S3UploadResponseDto;
import com.sparta.cookietalk.common.enums.UploadStatus;
import com.sparta.cookietalk.common.enums.UploadType;
import com.sparta.cookietalk.common.events.FileUploadStatusChangedEvent;
import com.sparta.cookietalk.common.utils.FileUtils;
import com.sparta.cookietalk.upload.components.Converter;
import java.io.File;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j(topic = "FileUploader")
@RequiredArgsConstructor
public class FileUploader {

    private final Converter<File, Optional<File>> hlsConverter;
    private final AmazonS3Uploader s3Uploader;

    private final FileUtils fileUtils;
    private final ApplicationEventPublisher eventPublisher;
    private final UploadFileRepository repository;

    @Async
    public void uploadVideo(File video, UploadFile uploadFile) {
        File convert = hlsConverter.convert(video).orElse(null);

        if (convert == null || !convert.exists() || convert.listFiles() == null) {
            setUploadFailedAndSave(uploadFile);
            return;
        }

        String prefixKey = UploadType.VIDEO.getKey() + "/" + fileUtils.getFilename(convert.getName());
        S3UploadResponseDto response = s3Uploader.uploadVideoToS3(prefixKey, convert.listFiles()).orElse(null);

        deleteFileIfExists(video);
        if(!validateResult(uploadFile, response)) {
            return;
        }

        setUploadCompleteAndSave(uploadFile, response);

    }

    @Async
    public void uploadFile(UploadType uploadType, File file, UploadFile uploadFile) {
        S3UploadResponseDto response = s3Uploader.uploadToS3(uploadType.getKey(), file).orElse(null);

        deleteFileIfExists(file);
        if(!validateResult(uploadFile, response)) {
            return;
        }

        setUploadCompleteAndSave(uploadFile, response);
    }

    private void deleteFileIfExists(File file) {
        if(file.exists()) {
            file.delete();
        }
    }

    private boolean validateResult(UploadFile file, S3UploadResponseDto response) {
        if(response == null){
            setUploadFailedAndSave(file);
            return false;
        }

        return true;
    }

    private void setUploadFailedAndSave(UploadFile file){
        file.updateStatus(UploadStatus.FAILED);
        repository.saveAndFlush(file);
        onStatusChanged(file.getId());
    }

    private void setUploadCompleteAndSave(UploadFile file, S3UploadResponseDto dto) {
        file.registS3Key(dto.getS3Key());
        file.registS3Url(dto.getS3Url());
        file.updateStatus(UploadStatus.COMPLETED);
        repository.saveAndFlush(file);
        onStatusChanged(file.getId());
        log.info("현재 File 상태 : {} / {}", file.getUploadType().name(), file.getStatus().name());
    }

    private void onStatusChanged(Long uploadFileId) {
        eventPublisher.publishEvent(new FileUploadStatusChangedEvent(this, uploadFileId));
    }
}
