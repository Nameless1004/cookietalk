package com.sparta.cookietalk.category.service;

import com.sparta.cookietalk.category.dto.CategoryResponse;
import com.sparta.cookietalk.category.dto.CategoryResponse.Detail;
import com.sparta.cookietalk.category.entity.Category;
import com.sparta.cookietalk.category.repository.CategoryRepository;
import com.sparta.cookietalk.category.repository.CookieCategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CookieCategoryRepository cookieCategoryRepository;

    public void getAllCookiesByCategoryId(long categoryId, int lastIndex) {

    }

    public CategoryResponse.All getAllCategory() {
        List<Category> all = categoryRepository.findAll();
        List<CategoryResponse.Detail> list = all.stream()
            .map(x -> new Detail(x.getId(), x.getName()))
            .toList();

        return new CategoryResponse.All(list);
    }
}
