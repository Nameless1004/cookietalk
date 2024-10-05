package com.sparta.cookietalk.channel.entity;

import com.sparta.cookietalk.amazon.S3UploadResponseDto;
import com.sparta.cookietalk.channel.dto.ChannelRequest;
import com.sparta.cookietalk.channel.dto.ChannelRequest.Update;
import com.sparta.cookietalk.cookie.entity.Cookie;
import com.sparta.cookietalk.series.entity.Series;
import com.sparta.cookietalk.upload.UploadFile;
import com.sparta.cookietalk.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Channel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upload_id")
    private UploadFile profileImage;

    private String githubUrl;
    private String blogUrl;
    private String businessEmail;
    private String description;

    @OneToMany(mappedBy = "channel")
    private List<Cookie> cookies = new ArrayList<>();

    @OneToMany(mappedBy = "channel")
    private List<Series> series = new ArrayList<>();

    public Channel(User user) {
        this.user = user;
        user.registChannel(this);
    }

    public void registProfileImage(UploadFile uploadFile) {
        this.profileImage = uploadFile;
    }

    public void updateProfile(ChannelRequest.Update request) {
        this.description = request.description();
        this.blogUrl = request.blogUrl();
        this.githubUrl = request.githubUrl();
        this.businessEmail = request.businessEmail();
    }
}
