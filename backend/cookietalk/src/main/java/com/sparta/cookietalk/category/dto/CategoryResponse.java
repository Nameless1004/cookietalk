package com.sparta.cookietalk.category.dto;

import com.sparta.cookietalk.category.dto.CategoryResponse.Create;
import com.sparta.cookietalk.category.dto.CategoryResponse.List;
import com.sparta.cookietalk.category.dto.CategoryResponse.Update;

public sealed interface CategoryResponse permits List, Create, Update {
    record List(Long id, String name) implements CategoryResponse {}

    record Create(Long id, String name) implements CategoryResponse {}

    record Update(Long id, String name) implements CategoryResponse {}
}
