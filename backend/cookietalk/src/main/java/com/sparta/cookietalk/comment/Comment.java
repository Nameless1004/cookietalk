package com.sparta.cookietalk.comment;

import com.sparta.cookietalk.common.entity.Timestamped;
import com.sparta.cookietalk.cookie.entity.Cookie;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Setter;

@Entity
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // 쿠키와 양방향 관계 설정
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cookie_id")
    private Cookie cookie;

}
