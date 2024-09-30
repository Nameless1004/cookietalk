package com.sparta.cookietalk.category.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sparta.cookietalk.category.dto.CategoryResponse.All;
import com.sparta.cookietalk.category.dto.CategoryResponse.Detail;
import java.util.List;

public sealed interface CategoryResponse permits Detail, All {
    record Detail(Long id, String name) implements CategoryResponse {
    }

    record All(List<Detail> list) implements CategoryResponse {}
}
