package com.sparta.cookietalk.cookie.entity;

import com.sparta.cookietalk.channel.entity.Channel;
import com.sparta.cookietalk.category.entity.CookieCategory;
import com.sparta.cookietalk.comment.entity.Comment;
import com.sparta.cookietalk.common.entity.Timestamped;
import com.sparta.cookietalk.common.enums.ProcessStatus;
import com.sparta.cookietalk.series.entity.SeriesCookie;
import com.sparta.cookietalk.upload.UploadFile;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cookie extends Timestamped {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private ProcessStatus proccessStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id" , nullable = false)
    private Channel channel;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", nullable = false)
    private UploadFile videoFile;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thumbnail_id", nullable = false)
    private UploadFile thumbnailFile;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attachment_id", nullable = true)
    private UploadFile attachmentFile; // zip 파일

    @OneToMany(mappedBy = "cookie", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CookieCategory> cookieCategories = new ArrayList<>();

    @OneToMany(mappedBy = "cookie", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<SeriesCookie> seriesCookies = new ArrayList<>();

    @Column(nullable = false)
    private Long cookieViews;


    @OneToMany(mappedBy = "cookie", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "cookie", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @BatchSize(size = 10)
    private List<UserRecentCookie> recentCookies = new ArrayList<>();

    /**
     * 비디오 등록
     * @param video
     */
    public void addVideo(UploadFile video) {
        this.videoFile = video;
    }

    /**
     * 썸네일 등록
     * @param thumbnail
     */
    public void addThumbnail(UploadFile thumbnail) {
        this.thumbnailFile = thumbnail;
    }

    /**
     * 레퍼런스 자료 등록
     * @param attachment
     */
    public void addAttachment(UploadFile attachment) {
        this.attachmentFile = attachment;
    }

    @Builder
    public Cookie(Channel channel, String title, String description, ProcessStatus status, UploadFile videoFile, UploadFile thumbnailFile, UploadFile attachmentFile) {
        this.channel = channel;
        this.title = title;
        this.description = description;
        this.proccessStatus = status;
        this.videoFile = videoFile;
        this.thumbnailFile = thumbnailFile;
        this.attachmentFile = attachmentFile;
        this.cookieViews = 0L;
    }

    public void updateProcessStatus(ProcessStatus processStatus) {
        this.proccessStatus = processStatus;
    }

    public void incrementView() {
        this.cookieViews++;
    }
}
