package com.sparta.cookietalk.security.filter;

import com.sparta.cookietalk.common.enums.TokenType;
import com.sparta.cookietalk.reissue.repository.RefreshRepository;
import com.sparta.cookietalk.security.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

@Slf4j(topic = "Logout")
public class JwtLogoutFilter extends GenericFilterBean {

    private final JwtUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public JwtLogoutFilter(JwtUtil jwtUtil, RefreshRepository refreshRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
        FilterChain filterChain) throws IOException, ServletException {
        doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse,
            filterChain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws IOException, ServletException {
        String requestURI = request.getRequestURI();

        // 모든 요청이 들어오기 때문에 logout 요청인지 확인
        // ----------------------------------------
        if (!requestURI.matches("^/api/users/logout$")) {
            filterChain.doFilter(request, response);
            return;
        }

        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }
        // ----------------------------------------

        // 리프레쉬 토큰 쿠키에서 가져오기
        String refreshToken = null;
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName()
                .equals(TokenType.REFRESH.name())) {
                refreshToken = cookie.getValue();
                break;
            }
        }

        refreshToken = jwtUtil.getDecodeToken(refreshToken);
        refreshToken = jwtUtil.substringToken(refreshToken);

        // 토큰 없으면
        if (refreshToken == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 토큰 만료 검사
        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 토큰이 REFRESH인지 검사
        String category = jwtUtil.getCategory(refreshToken);
        if (!category.equals(TokenType.REFRESH.name())) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // DB 저장되어있는지 검사
        boolean isExist = refreshRepository.existsByRefresh(refreshToken);
        if (!isExist) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        refreshRepository.deleteByRefresh(refreshToken);

        // Refresh 토큰 Cookie값 없애기
        Cookie cookie = new Cookie(TokenType.REFRESH.name(), null);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
