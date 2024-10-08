package com.sparta.cookietalk.series.entity;

import com.sparta.cookietalk.common.entity.Timestamped;
import com.sparta.cookietalk.cookie.entity.Cookie;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SeriesCookie extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id")
    private Series series;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cookie_id")
    private Cookie cookie;

    public SeriesCookie(Series series, Cookie cookie) {
        this.series = series;
        this.cookie = cookie;
        series.getSeriesCookies().add(this);
        cookie.getSeriesCookies().add(this);
    }
}
