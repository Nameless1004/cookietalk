package com.sparta.cookietalk.category.dto;

import com.sparta.cookietalk.category.dto.CategoryRequest.Create;
import com.sparta.cookietalk.category.dto.CategoryRequest.Update;

public sealed interface CategoryRequest permits Create, Update {
    record Create(String categoryName) implements CategoryRequest {}

    record Update(String categoryName) implements CategoryRequest {}
}
