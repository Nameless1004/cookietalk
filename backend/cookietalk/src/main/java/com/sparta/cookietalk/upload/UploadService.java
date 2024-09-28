package com.sparta.cookietalk.upload;

import com.sparta.cookietalk.amazon.AmazonS3Uploader;
import com.sparta.cookietalk.common.enums.UploadStatus;
import com.sparta.cookietalk.common.enums.UploadType;
import com.sparta.cookietalk.common.exceptions.FileUploadInProgressException;
import com.sparta.cookietalk.common.utils.FileUtils;
import com.sparta.cookietalk.upload.components.Converter;
import com.sparta.cookietalk.user.entity.User;
import java.io.File;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UploadService {

    private final FileUtils fileUtils;
    private final Converter<File, Optional<File>> hlsConverter;
    private final VideoUpload fileUploadAsync;
    private final AmazonS3Uploader s3Uploader;
    private final UploadFileRepository uploadFileRepository;

    public UploadFile uploadVideo(MultipartFile file) {
        File vod = fileUtils.saveTemp(UploadType.VIDEO, file);

        // s3_url은 나중에 업로드되면 지정
        UploadFile uploadFile = UploadFile.builder()
            .status(UploadStatus.WAITING)
            .uploadType(UploadType.VIDEO)
            .build();

        uploadFile = uploadFileRepository.saveAndFlush(uploadFile);
        fileUploadAsync.uploadVideo(vod, uploadFile);

        return uploadFile;
    }

    public UploadFile uploadFile(UploadType uploadType, MultipartFile multipartFile) {
        File file = fileUtils.saveTemp(uploadType, multipartFile);
        UploadFile uploadFile = UploadFile.builder()
            .uploadType(uploadType)
            .status(UploadStatus.WAITING)
            .build();

        uploadFile = uploadFileRepository.saveAndFlush(uploadFile);
        fileUploadAsync.uploadFile(uploadType, file, uploadFile);

        return uploadFile;
    }

    public void deleteFile(User user, Long fileId) {
        UploadFile file = uploadFileRepository.findByIdOrElseThorw(fileId);
        if (file.getStatus() == UploadStatus.WAITING) {
            throw new FileUploadInProgressException();
        }

        s3Uploader.deleteFile(file.getUploadType(), file.getS3Key());
        uploadFileRepository.delete(file);
    }
}
