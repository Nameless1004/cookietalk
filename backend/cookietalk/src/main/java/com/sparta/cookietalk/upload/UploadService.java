package com.sparta.cookietalk.upload;

import com.sparta.cookietalk.amazon.AmazonS3Uploader;
import com.sparta.cookietalk.amazon.HlsConverter;
import com.sparta.cookietalk.common.enums.UploadStatus;
import com.sparta.cookietalk.common.enums.UploadType;
import com.sparta.cookietalk.common.exceptions.ConvertFailedException;
import com.sparta.cookietalk.common.exceptions.FileUploadInProgressException;
import com.sparta.cookietalk.common.utils.FileUtils;
ㅁㅇimport com.sparta.cookietalk.upload.dto.UploadFileResponse;
import com.sparta.cookietalk.upload.dto.UploadFileResponse.Detail;
import com.sparta.cookietalk.user.entity.User;
import java.io.File;
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
    private final HlsConverter hlsConverter;
    private final AmazonS3Uploader s3Uploader;
    private final UploadFileRepository uploadFileRepository;

    public UploadFileResponse.Detail uploadVideo(User user, MultipartFile file) {
        File vod = fileUtils.saveTemp(UploadType.VIDEO, file);

        // s3_url은 나중에 업로드되면 지정

        UploadFile uploadFile = UploadFile.builder()
            .status(UploadStatus.WAITING)
            .uploadType(UploadType.VIDEO)
            .build();

        final UploadFile saveFile = uploadFileRepository.save(uploadFile);

        hlsConverter.convert(UploadType.VIDEO, vod)
            .exceptionally(ex -> {
                throw new ConvertFailedException("hls 변환 실패");
            })
            .thenCompose(x -> {
                log.info("변환 성공!!!!");
                String prefixKey =
                    UploadType.VIDEO.getKey() + "/" + fileUtils.getFilename(x.getName());
                log.info(prefixKey);
                log.info("업로드 시작!!!!");
                return s3Uploader.uploadVideoToS3(prefixKey, x.listFiles());
            })
            .thenAccept(result -> {
                log.info("업로드 성공!!!!");
                saveFile.registS3Url(result.getS3Url());
                saveFile.registS3Key(result.getS3Key());
                saveFile.updateStatus(UploadStatus.COMPLETED);
                uploadFileRepository.save(saveFile);
            })
            .exceptionally(ex -> {
                log.info("업로드 실패!!!!");
                saveFile.updateStatus(UploadStatus.FAILED);
                uploadFileRepository.save(saveFile);
                throw new IllegalStateException("S3 업로드 실패");
            });

        log.info("{}", saveFile.getId());
        return new Detail(saveFile.getId(), "");
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
