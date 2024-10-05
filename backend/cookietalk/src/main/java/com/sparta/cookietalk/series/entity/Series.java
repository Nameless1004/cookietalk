package com.sparta.cookietalk.series.entity;

import com.sparta.cookietalk.channel.entity.Channel;
import com.sparta.cookietalk.common.entity.Timestamped;
import com.sparta.cookietalk.cookie.entity.Cookie;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Series extends Timestamped {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @OneToMany(mappedBy = "series", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<SeriesCookie> seriesCookies;

    public Series(Channel channel, String title){
        this.title = title;
        this.channel = channel;
        channel.getSeries().add(this);
    }

    public void updateTitle(String newTitle){
        this.title = newTitle;
    }
}
