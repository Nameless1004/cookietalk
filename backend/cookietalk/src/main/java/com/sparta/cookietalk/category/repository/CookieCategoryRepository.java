package com.sparta.cookietalk.category.repository;

import com.sparta.cookietalk.category.entity.CookieCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CookieCategoryRepository extends JpaRepository<CookieCategory, Long>, CookieCategoryCustomRepository {

}
