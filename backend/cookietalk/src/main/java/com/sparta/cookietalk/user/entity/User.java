package com.sparta.cookietalk.user.entity;

import com.sparta.cookietalk.channel.entity.Channel;
import com.sparta.cookietalk.comment.entity.Comment;
import com.sparta.cookietalk.common.enums.UserRole;
import com.sparta.cookietalk.cookie.entity.UserRecentCookie;
import com.sparta.cookietalk.user.UserEventListener;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Getter
@Entity
@EntityListeners(UserEventListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private Long kakaoId;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Channel channel;

    @OneToMany(mappedBy = "authUser")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @BatchSize(size = 10)
    private List<UserRecentCookie> recentCookies = new ArrayList<>();

    public User(String username, String password, String email,String nickname,
        UserRole role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.role = role;
    }

    @Builder
    public User(String username, String password, String email,String nickname,
        UserRole role, Long kakaoId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.role = role;
        this.kakaoId = kakaoId;
    }

    public User updateKakaoId(Long kakaoId) {
        this.kakaoId = kakaoId;
        return this;
    }

    public void registChannel(Channel channel) {
        this.channel = channel;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true; // 메모리 주소가 같으면 true
//        if (o == null || getClass() != o.getClass()) return false;
//        User user = (User) o; // 객체 캐스팅
//        return id != null && id.equals(user.id);
//    }
}
