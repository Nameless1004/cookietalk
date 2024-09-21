package com.sparta.cookietalk.cookie.service;

import com.sparta.cookietalk.channel.entity.Channel;
import com.sparta.cookietalk.common.enums.ProccessStatus;
import com.sparta.cookietalk.common.enums.UploadStatus;
import com.sparta.cookietalk.common.exceptions.FileUploadInProgressException;
import com.sparta.cookietalk.common.exceptions.InvalidRequestException;
import com.sparta.cookietalk.cookie.dto.CookieRequest;
import com.sparta.cookietalk.cookie.dto.CookieResponse;
import com.sparta.cookietalk.cookie.dto.CookieResponse.List;
import com.sparta.cookietalk.cookie.entity.Cookie;
import com.sparta.cookietalk.cookie.repository.CookieRepository;
import com.sparta.cookietalk.upload.UploadFile;
import com.sparta.cookietalk.upload.UploadFileRepository;
import com.sparta.cookietalk.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CookieService {

    private final CookieRepository cookieRepository;

    private final UploadFileRepository uploadFileRepository;

    public CookieResponse.Create createCookie(User auth, CookieRequest.Create requestDto) {
        UploadFile video = uploadFileRepository.findByIdOrElseThorw(requestDto.videoFileId());
        UploadFile thumbnail = uploadFileRepository.findByIdOrElseThorw(requestDto.thumbnailFileId());
        UploadFile attachment = requestDto.attachmentFileId() == null ? null : uploadFileRepository.findByIdOrElseThorw(requestDto.attachmentFileId());

        fileUploadCompleteCheck(video, thumbnail, attachment);
        Channel channel = auth.getChannel();
        Cookie newCookie = Cookie.builder()
            .channel(channel)
            .videoFile(video)
            .thumbnailFile(thumbnail)
            .attachmentFile(attachment)
            .status(ProccessStatus.SUCCESS)
            .title(requestDto.title())
            .description(requestDto.description())
            .build();
        newCookie = cookieRepository.save(newCookie);

        return new CookieResponse.Create(newCookie.getId());
    }

    private void fileUploadCompleteCheck(UploadFile... video) {
        for (UploadFile file : video) {
            if(file == null) continue;
            if(file.getStatus() != UploadStatus.COMPLETED) {
                throw new FileUploadInProgressException();
            }
        }
    }

    public CookieResponse.Detail getCookie(Long cookieId) {
        if(!cookieRepository.existsById(cookieId)) {
            throw new InvalidRequestException("존재하지 않는 쿠키입니다.");
        }

        return cookieRepository.getCookieDetails(cookieId);
    }

    public Page<List> getAllCookies(Long channelId, int page, int size) {
        Pageable pageRequest = PageRequest.of(page, size);
        Page<List> c = cookieRepository.getCookieListByChannelId(channelId, pageRequest);
        return null;
    }
}
