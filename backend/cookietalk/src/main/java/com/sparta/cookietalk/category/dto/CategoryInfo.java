package com.sparta.cookietalk.category.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class CategoryInfo {
    private Long id;
    private String name;

    @QueryProjection
    public CategoryInfo(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
