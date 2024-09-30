package com.sparta.cookietalk.category.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CookieCategoryCustomRepositoryImpl implements CookieCategoryCustomRepository{
    private final JPAQueryFactory queryFactory;
}
