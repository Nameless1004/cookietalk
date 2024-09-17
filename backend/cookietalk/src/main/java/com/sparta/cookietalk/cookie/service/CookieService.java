package com.sparta.cookietalk.cookie.service;

import com.sparta.cookietalk.amazon.AmazonS3Uploader;
import com.sparta.cookietalk.amazon.HlsConverter;
import com.sparta.cookietalk.common.enums.UploadStatus;
import com.sparta.cookietalk.common.enums.UploadType;
import com.sparta.cookietalk.common.exceptions.ConvertFailedException;
import com.sparta.cookietalk.common.exceptions.FileUploadInProgressException;
import com.sparta.cookietalk.common.utils.FileUtils;
import com.sparta.cookietalk.cookie.dto.CookieCreateRequestDto;
import com.sparta.cookietalk.cookie.entity.Cookie;
import com.sparta.cookietalk.cookie.repository.CookieRepository;
import com.sparta.cookietalk.upload.UploadFile;
import com.sparta.cookietalk.upload.UploadFileRepository;
import com.sparta.cookietalk.user.entity.User;
import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CookieService {

    private final CookieRepository cookieRepository;
    private final HlsConverter hlsConverter;
    private final AmazonS3Uploader s3Uploader;
    private final FileUtils fileUtils;

    private final UploadFileRepository uploadFileRepository;

    public void createCookie(User creator, CookieCreateRequestDto requestDto) {

        UploadFile video = uploadFileRepository.findByIdOrElseThorw(requestDto.getVideoFileId());
        UploadFile thumbnail = uploadFileRepository.findByIdOrElseThorw(requestDto.getVideoFileId());
        UploadFile referenceFile = requestDto.getReferenceFileId() == null ? null : uploadFileRepository.findByIdOrElseThorw(requestDto.getReferenceFileId());

        fileUploadCompleteCheck(video, thumbnail, referenceFile);

        Cookie newCookie = Cookie.builder()
            // .creator(creator)
            // .title(requestDto.getTitle())
            // .description(requestDto.getDescription())
            .build();
    }

    private void fileUploadCompleteCheck(UploadFile... video) {
        for (UploadFile file : video) {
            if(file == null) continue;
            if(file.getStatus() != UploadStatus.COMPLETED) {
                throw new FileUploadInProgressException();
            }
        }
    }

    public void deleteCookie(User user, Cookie cookie) {
        // 해당 유저만 삭제 가능

    }

    /**
     * 해당 쿠키 정보
     *
     * @param cookieId
     */
    public void GetCookieInfo(Long cookieId) {

    }

    /**
     * 해당 유저의 모든 쿠키 정보
     *
     * @param user
     */
    public void GetCookieInfos(User user, Pageable pageable) {

    }
}
