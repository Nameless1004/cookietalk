package com.sparta.cookietalk.upload;

import com.sparta.cookietalk.amazon.AmazonS3Uploader;
import com.sparta.cookietalk.common.enums.UploadStatus;
import com.sparta.cookietalk.common.enums.UploadType;
import com.sparta.cookietalk.common.exceptions.FileUploadInProgressException;
import com.sparta.cookietalk.common.exceptions.ImageResizingFailedException;
import com.sparta.cookietalk.common.utils.FileUtils;
import com.sparta.cookietalk.user.entity.User;
import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UploadService {

    private final FileUtils fileUtils;
    private final FileUploader fileUploader;
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
        fileUploader.uploadVideo(vod, uploadFile);
        return uploadFile;
    }

    /**
     * 파일 비동기 업로드
     * @param uploadType
     * @param multipartFile
     * @return
     */
    public UploadFile uploadFileAsync(UploadType uploadType, MultipartFile multipartFile) {
        File file = fileUtils.saveTemp(uploadType, multipartFile);
        UploadFile uploadFile = UploadFile.builder()
            .uploadType(uploadType)
            .status(UploadStatus.WAITING)
            .build();

        uploadFile = uploadFileRepository.saveAndFlush(uploadFile);
        fileUploader.uploadFileAsync(uploadType, file, uploadFile);

        return uploadFile;
    }

    /**
     * 이미지 비동기 업로드
     * @param multipartFile
     * @return
     */
    public UploadFile uploadImageAsync(int width, int height, MultipartFile multipartFile) {
        File file = fileUtils.saveTemp(UploadType.IMAGE, multipartFile);

        try {
            Thumbnails.of(file)
                .size(width, height)
                .keepAspectRatio(false)
                .toFile(file);
        } catch(IOException e) {
            log.error("리사이징 실패 파일: {}", file.getName());
        }

        UploadFile uploadFile = UploadFile.builder()
            .uploadType(UploadType.IMAGE)
            .status(UploadStatus.WAITING)
            .build();

        uploadFile = uploadFileRepository.saveAndFlush(uploadFile);
        fileUploader.uploadFileAsync(UploadType.IMAGE, file, uploadFile);

        return uploadFile;
    }

    /**
     * 파일 업로드
     * @param uploadType
     * @param multipartFile
     * @return
     */
    public UploadFile uploadFile(UploadType uploadType, MultipartFile multipartFile) {
        File file = fileUtils.saveTemp(uploadType, multipartFile);
        UploadFile uploadFile = UploadFile.builder()
            .uploadType(uploadType)
            .status(UploadStatus.WAITING)
            .build();

        uploadFile = uploadFileRepository.saveAndFlush(uploadFile);
        fileUploader.uploadFile(uploadType, file, uploadFile);

        return uploadFile;
    }

    public void deleteFile(Long fileId) {
        UploadFile file = uploadFileRepository.findByIdOrElseThorw(fileId);
        if (file.getStatus() == UploadStatus.WAITING) {
            throw new FileUploadInProgressException();
        }

        s3Uploader.deleteFile(file.getUploadType(), file.getS3Key());
        uploadFileRepository.delete(file);
    }
}
