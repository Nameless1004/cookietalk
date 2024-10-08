package com.sparta.cookietalk.category.service;

import com.sparta.cookietalk.category.dto.CategoryRequest;
import com.sparta.cookietalk.category.dto.CategoryResponse;
import com.sparta.cookietalk.category.entity.Category;
import com.sparta.cookietalk.category.repository.CategoryRepository;
import com.sparta.cookietalk.common.exceptions.InvalidRequestException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryResponse.List> getAllCategory() {
        List<Category> all = categoryRepository.findAll();
        List<CategoryResponse.List> result = all.stream()
            .map(x -> new CategoryResponse.List(x.getId(), x.getName()))
            .toList();

        return result;
    }

    public CategoryResponse.Create createCategory(CategoryRequest.Create request) {
        Category category = new Category(request.categoryName());
        category = categoryRepository.save(category);
        return new CategoryResponse.Create(category.getId(), category.getName());
    }

    public CategoryResponse.Update updateCategory(Long categoryId, CategoryRequest.Update request) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new InvalidRequestException("존재하지 않는 카테고리입니다."));

        category.updateName(request.categoryName());
        return new CategoryResponse.Update(categoryId, category.getName());
    }

    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new InvalidRequestException("존재하지 않는 카테고리입니다."));

        categoryRepository.delete(category);
    }
}
