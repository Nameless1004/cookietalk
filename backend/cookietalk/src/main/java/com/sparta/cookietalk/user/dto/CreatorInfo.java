package com.sparta.cookietalk.user.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class CreatorInfo {
    private Long userId;
    private String nickname;

    @QueryProjection
    public CreatorInfo(Long userId, String nickname) {
        this.userId = userId;
        this.nickname = nickname;
    }
}
