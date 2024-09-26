package com.sparta.cookietalk.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.cookietalk.common.dto.ResponseDto;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.filter.GenericFilterBean;

@Slf4j(topic = "Logout")
public class JwtLogoutFilter extends GenericFilterBean {

    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, Object> redisTemplate;

    public JwtLogoutFilter(JwtUtil jwtUtil, RedisTemplate<String, Object> redisTemplate) {
        this.jwtUtil = jwtUtil;
        this.redisTemplate = redisTemplate;
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

        // 헤더에 액세스 토큰 있는지 검사
        String accessToken = jwtUtil.getAccessTokenFromRequestHeader(request);
        ObjectMapper mapper = new ObjectMapper();
        if(!jwtUtil.canSubstringToken(accessToken)){
            ResponseDto<Void> errorResponse = ResponseDto.of(HttpStatus.BAD_REQUEST,
                "액세스 토큰을 헤더에 담아주세요.", null);

            String msg = mapper.writeValueAsString(mapper.writeValueAsString(errorResponse));
            response.getWriter().write(msg);
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // prefix 제거
        accessToken = jwtUtil.substringToken(accessToken);
        String username = jwtUtil.getUsername(accessToken);

        String refreshToken = (String) redisTemplate.opsForValue().get(username);

        if(refreshToken != null){
            redisTemplate.delete(username);
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
