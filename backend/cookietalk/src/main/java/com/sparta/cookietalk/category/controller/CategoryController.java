package com.sparta.cookietalk.category.controller;

import com.sparta.cookietalk.category.dto.CategoryResponse;
import com.sparta.cookietalk.category.dto.CategoryResponse.All;
import com.sparta.cookietalk.category.service.CategoryService;
import com.sparta.cookietalk.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/api/categories")
    public ResponseEntity<ResponseDto<CategoryResponse.All>> getAllCategories(){
        CategoryResponse.All allCategory = categoryService.getAllCategory();
        return ResponseDto.toEntity(HttpStatus.OK, allCategory);
    }
}
