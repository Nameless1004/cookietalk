package com.sparta.cookietalk.upload;

import com.sparta.cookietalk.amazon.AmazonS3Uploader;
import com.sparta.cookietalk.amazon.S3UploadResponseDto;
import com.sparta.cookietalk.common.enums.UploadStatus;
import com.sparta.cookietalk.common.enums.UploadType;
import com.sparta.cookietalk.common.utils.FileUtils;
import com.sparta.cookietalk.upload.components.Converter;
import java.io.File;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileUploader {

    private final Converter<File, Optional<File>> hlsConverter;
    private final AmazonS3Uploader s3Uploader;

    private final FileUtils fileUtils;
    private final UploadFileRepository repository;

    @Async
    public void uploadVideo(File video, UploadFile uploadFile) {
        File convert = hlsConverter.convert(video).orElse(null);

        if (convert == null || !convert.exists() || convert.listFiles() == null) {
            uploadFile.updateStatus(UploadStatus.FAILED);
            repository.saveAndFlush(uploadFile);
            return;
        }

        String prefixKey = UploadType.VIDEO.getKey() + "/" + fileUtils.getFilename(convert.getName());
        S3UploadResponseDto response = s3Uploader.uploadVideoToS3(prefixKey, convert.listFiles()).orElse(null);

        if(response == null){
            uploadFile.updateStatus(UploadStatus.FAILED);
            repository.saveAndFlush(uploadFile);
            return;
        }

        uploadFile.registS3Url(response.getS3Url());
        uploadFile.registS3Key(response.getS3Key());
        uploadFile.updateStatus(UploadStatus.COMPLETED);
        repository.saveAndFlush(uploadFile);

        if(video.exists()) {
            video.delete();
        }
    }

    @Async
    public void uploadFile(UploadType uploadType, File file, UploadFile uploadFile) {

        S3UploadResponseDto res = s3Uploader.uploadToS3(uploadType.getKey(), file).orElse(null);

        if(file.exists()){
            file.delete();
        }

        if(res == null){
            uploadFile.updateStatus(UploadStatus.FAILED);
            repository.saveAndFlush(uploadFile);
            return;
        }

        uploadFile.registS3Key(res.getS3Key());
        uploadFile.registS3Url(res.getS3Url());
        uploadFile.updateStatus(UploadStatus.COMPLETED);
        repository.saveAndFlush(uploadFile);
    }
}
