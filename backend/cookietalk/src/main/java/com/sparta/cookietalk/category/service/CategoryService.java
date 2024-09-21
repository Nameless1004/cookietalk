package com.sparta.cookietalk.category.service;

import com.sparta.cookietalk.category.repository.CategoryRepository;
import com.sparta.cookietalk.category.repository.CookieCategoryRepository;
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
}
