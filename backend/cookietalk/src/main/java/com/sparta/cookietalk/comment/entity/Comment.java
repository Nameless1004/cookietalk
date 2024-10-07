package com.sparta.cookietalk.comment.entity;

import com.sparta.cookietalk.common.entity.Timestamped;
import com.sparta.cookietalk.cookie.entity.Cookie;
import com.sparta.cookietalk.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cookie_id")
    private Cookie cookie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User authUser;

    @Builder
    public Comment(Cookie cookie, User user, String contents) {
        this.cookie = cookie;
        this.authUser = user;
        this.contents = contents;
        cookie.getComments().add(this);
        user.getComments().add(this);
    }

    public void updateContents(String contents) {
        this.contents = contents;
    }
}
