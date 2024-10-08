package com.sparta.cookietalk.cookie.entity;

import com.sparta.cookietalk.common.entity.Timestamped;
import com.sparta.cookietalk.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRecentCookie {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cookie_id", unique = true)
    private Cookie cookie;

    @Column
    private LocalDateTime viewAt;

    @Builder
    public UserRecentCookie(User user, Cookie cookie) {
        this.user = user;
        this.cookie = cookie;
        this.viewAt = LocalDateTime.now();
    }

    public void updateViewAt() {
        this.viewAt = LocalDateTime.now();
    }
}
