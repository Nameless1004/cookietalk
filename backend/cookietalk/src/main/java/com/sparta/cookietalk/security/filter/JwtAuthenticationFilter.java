package com.sparta.cookietalk.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.cookietalk.common.enums.TokenType;
import com.sparta.cookietalk.common.enums.UserRole;
import com.sparta.cookietalk.reissue.entity.RefreshEntity;
import com.sparta.cookietalk.reissue.repository.RefreshRepository;
import com.sparta.cookietalk.security.JwtUtil;
import com.sparta.cookietalk.security.UserDetailsImpl;
import com.sparta.cookietalk.user.dto.LoginRequestDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, RefreshRepository refreshRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
        setFilterProcessesUrl("/api/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException {
        log.info("::: JwtAuthenticationFilter :::");
        log.info("로그인 시도");
        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                    requestDto.getUsername(),
                    requestDto.getPassword(),
                    null
                )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authResult)
        throws IOException, ServletException {
        log.info("로그인 성공 및 JWT 생성");
        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();
        String username = userDetails.getUsername();
        UserRole role = userDetails.getUser()
            .getRole();

        String accessToken = jwtUtil.createToken(TokenType.ACCESS, username, role);
        String refreshToken = jwtUtil.createToken(TokenType.REFRESH, username, role);

        // Refresh 레포지토리에 저장
        // 새로 발급한 토큰에 prefix를 제거 해준 후 저장
        addRefreshEntity(username, jwtUtil.substringToken(refreshToken));

        jwtUtil.addTokenToHeader(response, accessToken);
        jwtUtil.addCookie(response, TokenType.REFRESH, refreshToken);
        response.setStatus(HttpStatus.OK.value());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, AuthenticationException failed)
        throws IOException, ServletException {
        log.info(failed.getMessage());
        log.info("로그인 실패");
        response.setStatus(401);
    }

    /**
     * Encode가 안된 토큰을 넣어줘야합니다.
     *
     * @param username
     * @param refreshToken
     */
    private void addRefreshEntity(String username, String refreshToken) {
        Long refreshTokenTime = TokenType.REFRESH.getLifeTime();
        Date date = new Date(System.currentTimeMillis() + refreshTokenTime);

        RefreshEntity refreshEntity = RefreshEntity.builder()
            .username(username)
            .refresh(refreshToken)
            .expiration(date.toString())
            .build();

        refreshRepository.save(refreshEntity);
    }
}