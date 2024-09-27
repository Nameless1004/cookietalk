package com.sparta.cookietalk.cookie.repository;

import com.sparta.cookietalk.cookie.dto.CookieResponse.Detail;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CookieCustomRepository {
    Detail getCookieDetails(Long id);

}
