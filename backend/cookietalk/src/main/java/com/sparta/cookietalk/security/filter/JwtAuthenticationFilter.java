package com.sparta.cookietalk.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.cookietalk.common.enums.TokenType;
import com.sparta.cookietalk.common.enums.UserRole;
import com.sparta.cookietalk.security.JwtUtil;
import com.sparta.cookietalk.security.UserDetailsImpl;
import com.sparta.cookietalk.user.dto.LoginRequestDto;
import com.sparta.cookietalk.user.dto.UserResponse;
import com.sparta.cookietalk.user.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, Object> redisTemplate;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, RedisTemplate<String, Object> redisTemplate) {
        this.jwtUtil = jwtUtil;
        this.redisTemplate = redisTemplate;
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

        // 토큰 생성
        String accessToken = jwtUtil.createAccessToken(username, role, false);
        String refreshToken = jwtUtil.createRefreshToken(username, role, false);

        // 유저 정보 response
        ObjectMapper objectMapper = new ObjectMapper();
        User user = userDetails.getUser();
        UserResponse.Login loginInfo = new UserResponse.Login(user, accessToken, refreshToken);
        System.out.println("info = " + loginInfo.toString());
        response.setContentType("application/json;charset=UTF-8");
        String s = objectMapper.writeValueAsString(loginInfo);
        System.out.println("s = " + s);

        // 로그인 정보(아이디, 이름, 액세스토큰, 리프레쉬 토큰) Body에 담아서 보냄
        response.getWriter().write(s);

        // 레디스에 리프레쉬 토큰 저장
        redisTemplate.opsForValue().set(username, refreshToken, TokenType.REFRESH.getLifeTime(), TimeUnit.MILLISECONDS);

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
}