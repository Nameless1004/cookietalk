package com.sparta.cookietalk.cookie.repository;

import com.sparta.cookietalk.cookie.entity.Cookie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CookieRepository extends JpaRepository<Cookie, Long> {

}
