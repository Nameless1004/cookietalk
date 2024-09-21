package com.sparta.cookietalk.category.entity;

import com.sparta.cookietalk.cookie.entity.Cookie;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CookieCategory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Cookie cookie;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    public CookieCategory(Cookie cookie, Category category) {
        this.cookie = cookie;
        this.category = category;
        cookie.getCookieCategories().add(this);
        category.getCookieCategories().add(this);
    }
}
