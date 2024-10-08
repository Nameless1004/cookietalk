package com.sparta.cookietalk.cookie.repository;

import com.sparta.cookietalk.cookie.entity.UserRecentCookie;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRecentCookieRepository extends JpaRepository<UserRecentCookie, Long> {

    Optional<UserRecentCookie> findByCookieId(Long id);
}
