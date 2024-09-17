package com.sparta.cookietalk.cookie.entity;

import com.sparta.cookietalk.comment.Comment;
import com.sparta.cookietalk.common.entity.Timestamped;
import com.sparta.cookietalk.common.enums.ProccessStatus;
import com.sparta.cookietalk.upload.UploadFile;
import com.sparta.cookietalk.user.entity.User;
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
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private ProccessStatus proccessStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User creator;

    @OneToMany(mappedBy = "cookie")
    private List<Comment> comments = new ArrayList<>();

    @OneToOne(mappedBy = "cookie", fetch = FetchType.LAZY)
    private UploadFile video;

    @OneToOne(mappedBy = "cookie", fetch = FetchType.LAZY)
    private UploadFile thumbnail;

    @OneToOne(mappedBy = "cookie", fetch = FetchType.LAZY)
    private UploadFile referenceFile; // zip 파일

    /**
     * 댓글 추가
     * @param comment
     */
    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setCookie(this);
    }

    /**
     * 비디오 등록
     * @param video
     */
    public void addVideo(UploadFile video) {
        this.video = video;
    }

    /**
     * 썸네일 등록
     * @param thumbnail
     */
    public void addThumbnail(UploadFile thumbnail) {
        this.thumbnail = thumbnail;
    }

    /**
     * 레퍼런스 자료 등록
     * @param referenceFile
     */
    public void addReferenceFile(UploadFile referenceFile) {
        this.referenceFile = referenceFile;
    }

    @Builder
    public Cookie(User creator, String title, String description, ProccessStatus status, UploadFile video) {
        this.creator = creator;
        this.title = title;
        this.description = description;
        this.proccessStatus = status;
        this.video = video;
    }
}
