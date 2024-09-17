package com.sparta.cookietalk.user.entity;

import com.sparta.cookietalk.common.enums.UserRole;
import com.sparta.cookietalk.cookie.entity.Cookie;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
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

    @OneToMany(mappedBy = "creator")
    private List<Cookie> cookies = new ArrayList<>();

    public User(String username, String password, String email,String nickname,
        UserRole role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.role = role;
    }

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

}
