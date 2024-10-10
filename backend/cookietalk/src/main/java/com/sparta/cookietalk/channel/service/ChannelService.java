package com.sparta.cookietalk.channel.service;

import com.sparta.cookietalk.amazon.AmazonS3Uploader;
import com.sparta.cookietalk.amazon.S3UploadResponseDto;
import com.sparta.cookietalk.channel.dto.ChannelRequest.Update;
import com.sparta.cookietalk.channel.dto.ChannelResponse;
import com.sparta.cookietalk.channel.entity.Channel;
import com.sparta.cookietalk.channel.repository.ChannelRepository;
import com.sparta.cookietalk.common.enums.UploadStatus;
import com.sparta.cookietalk.common.enums.UploadType;
import com.sparta.cookietalk.common.exceptions.AuthException;
import com.sparta.cookietalk.common.exceptions.ImageResizingFailedException;
import com.sparta.cookietalk.common.exceptions.InvalidRequestException;
import com.sparta.cookietalk.common.exceptions.S3UploadFailedException;
import com.sparta.cookietalk.common.utils.FileUtils;
import com.sparta.cookietalk.security.AuthUser;
import com.sparta.cookietalk.upload.UploadFile;
import com.sparta.cookietalk.upload.UploadFileRepository;
import com.sparta.cookietalk.upload.UploadService;
import com.sparta.cookietalk.user.entity.User;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final AmazonS3Uploader s3Uploader;
    private final UploadFileRepository uploadFileRepository;
    private final UploadService uploadService;
    private final FileUtils fileUtils;

    @Transactional(readOnly = true)
    public ChannelResponse.Profile getChannelProfile(Long userId){
        Channel channel = channelRepository.findChannelWithUserByUserId(userId)
            .orElseThrow(() -> new InvalidRequestException("존재하지 않는 유저입니다."));
        User user = channel.getUser();
        String profileImageUrl = channel.getProfileImage() == null ? "": channel.getProfileImage().getS3Url();

        return ChannelResponse.Profile.builder()
            .channelId(channel.getId())
            .userId(user.getId())
            .nickname(user.getNickname())
            .description(channel.getDescription())
            .profileImageUrl(profileImageUrl)
            .blogUrl(channel.getBlogUrl())
            .githubUrl(channel.getGithubUrl())
            .businessEmail(channel.getBusinessEmail())
            .build();
    }

    public ChannelResponse.Profile updateProfile(long userId, MultipartFile profile, Update request, AuthUser authUser) {
        Channel channel = channelRepository.findChannelWithUserByUserId(authUser.getUserId())
            .orElseThrow(() -> new InvalidRequestException("존재하지 않는 채널입니다."));


        if(authUser.getUserId() != userId || authUser.getUserId() != channel.getUser().getId()) {
            throw new AuthException("수정 권한이 없습니다.");
        }

        if(profile == null) {
            if(channel.getProfileImage() != null && StringUtils.hasText(channel.getProfileImage().getS3Url())) {
                s3Uploader.deleteFile(UploadType.IMAGE, channel.getProfileImage().getS3Key());
                UploadFile profileImage = channel.getProfileImage();
                uploadFileRepository.delete(profileImage);
                channel.registProfileImage(null);
            }
        }


        if(profile != null){
            File profileTempFile = fileUtils.saveTemp(UploadType.IMAGE, profile);
            try {
                Thumbnails.of(profileTempFile).size(320, 320).keepAspectRatio(false).toFile(profileTempFile);
            } catch (IOException e) {
                if(profileTempFile.exists()) {
                    profileTempFile.delete();
                }

                log.error("[프로필 썸네일 변환 실패] 채널 아이디: {}", channel.getId());
                throw new ImageResizingFailedException("프로필 썸네일 변환 실패");
            }

            String prefixKey = UploadType.IMAGE.getKey() + "/" + "user_" + authUser.getUserId() + "profile";
            if(channel.getProfileImage() == null){
                S3UploadResponseDto response = s3Uploader.uploadFileToS3(prefixKey, profileTempFile)
                    .orElseThrow(S3UploadFailedException::new);
                UploadFile uploadFile = new UploadFile(UploadType.IMAGE, UploadStatus.COMPLETED, response.getS3Key(), response.getS3Url());
                uploadFile = uploadFileRepository.save(uploadFile);
                channel.registProfileImage(uploadFile);
            } else {
                s3Uploader.deleteFile(UploadType.IMAGE, channel.getProfileImage().getS3Key());
                S3UploadResponseDto response = s3Uploader.uploadFileToS3(prefixKey, profileTempFile)
                    .orElseThrow(S3UploadFailedException::new);

                channel.getProfileImage().updateS3(response);
            }
        }

        channel.updateProfile(request);
        channel = channelRepository.save(channel);
        String profileUrl = channel.getProfileImage() == null ? null : channel.getProfileImage().getS3Url();
        return ChannelResponse.Profile.builder()
            .channelId(channel.getId())
            .userId(channel.getUser().getId())
            .nickname(channel.getUser().getNickname())
            .description(channel.getDescription())
            .profileImageUrl(profileUrl)
            .blogUrl(channel.getBlogUrl())
            .githubUrl(channel.getGithubUrl())
            .businessEmail(channel.getBusinessEmail())
            .build();
    }
}
