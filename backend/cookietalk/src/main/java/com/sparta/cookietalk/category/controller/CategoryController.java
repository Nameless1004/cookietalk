package com.sparta.cookietalk.category.controller;

import com.sparta.cookietalk.category.dto.CategoryRequest;
import com.sparta.cookietalk.category.dto.CategoryResponse;
import com.sparta.cookietalk.common.enums.UserRole;
import com.sparta.cookietalk.common.enums.UserRole.Authority;
import java.util.List;
import com.sparta.cookietalk.category.service.CategoryService;
import com.sparta.cookietalk.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Secured({ Authority.ADMIN })
    @GetMapping("/api/v1/categories")
    public ResponseEntity<ResponseDto<List<CategoryResponse.List>>> getAllCategories(){
        return ResponseDto.toEntity(HttpStatus.OK, categoryService.getAllCategory());
    }

    @Secured({ Authority.ADMIN })
    @PostMapping("/api/v1/categories")
    public ResponseEntity<ResponseDto<CategoryResponse>> createCategory(@RequestBody CategoryRequest.Create categoryRequest){
        return ResponseDto.toEntity(HttpStatus.OK, categoryService.createCategory(categoryRequest));
    }

    @Secured({ Authority.ADMIN })
    @PatchMapping("/api/v1/categories/{categoryId}")
    public ResponseEntity<ResponseDto<CategoryResponse.Update>> updateCategory(
        @PathVariable("categoryId") Long categoryId,
        @RequestBody CategoryRequest.Update request) {

        return ResponseDto.toEntity(HttpStatus.OK, categoryService.updateCategory(categoryId, request));
    }

    @Secured({ Authority.ADMIN })
    @DeleteMapping("/api/v1/categories/{categoryId}")
    public ResponseEntity<ResponseDto<Void>> deleteCategory(
        @PathVariable("categoryId") Long categoryId) {

        categoryService.deleteCategory(categoryId);
        return ResponseDto.toEntity(HttpStatus.OK, "성공적으로 삭제되었습니다.", null);
    }
}
