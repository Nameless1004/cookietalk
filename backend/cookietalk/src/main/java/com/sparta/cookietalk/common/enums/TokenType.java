package com.sparta.cookietalk.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TokenType {
    ACCESS(345600000), // 10min
    REFRESH(345600000); // 24hour

    private final long lifeTime;
}