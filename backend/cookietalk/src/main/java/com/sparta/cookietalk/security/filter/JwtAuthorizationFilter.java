package com.sparta.cookietalk.security.filter;

import com.sparta.cookietalk.common.enums.TokenType;
import com.sparta.cookietalk.security.JwtUtil;
import com.sparta.cookietalk.security.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
        FilterChain filterChain) throws ServletException, IOException {
        // 헤더 검증
        String accessToken = jwtUtil.getAccessTokenFromRequestHeader(req);
        if (accessToken == null) {
            filterChain.doFilter(req, res);
            return;
        }

        accessToken = jwtUtil.substringToken(accessToken);
        log.info(accessToken);

        // 토큰 만료 검증
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            // response body
            PrintWriter writer = res.getWriter();
            writer.println("access token expired");

            // response status code
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 토큰이 access인지 확인(발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(accessToken);
        if (!category.equals(TokenType.ACCESS.name())) {

            // reponse body
            PrintWriter writer = res.getWriter();
            writer.println("invalid access token");
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String username = jwtUtil.getUsername(accessToken);

        Authentication authentication = createAuthentication(username);
        setAuthentication(authentication);

        filterChain.doFilter(req, res);
    }

    // 인증 처리
    public void setAuthentication(Authentication authentication) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null,
            userDetails.getAuthorities());
    }
}