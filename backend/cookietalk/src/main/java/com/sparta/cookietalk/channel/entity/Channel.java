package com.sparta.cookietalk.channel.entity;

import com.sparta.cookietalk.cookie.entity.Cookie;
import com.sparta.cookietalk.series.entity.Series;
import com.sparta.cookietalk.upload.UploadFile;
import com.sparta.cookietalk.user.entity.User;
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

    private String description;

    @OneToMany(mappedBy = "channel")
    private List<Cookie> cookies = new ArrayList<>();

    @OneToMany(mappedBy = "channel")
    private List<Series> series = new ArrayList<>();

    public Channel(User user) {
        this.user = user;
        user.registChannel(this);
    }
}
