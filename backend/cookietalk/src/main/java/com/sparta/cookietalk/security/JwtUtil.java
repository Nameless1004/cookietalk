package com.sparta.cookietalk.security;

import com.sparta.cookietalk.common.enums.TokenType;
import com.sparta.cookietalk.common.enums.UserRole;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

@Slf4j(topic = "JWT Util")
@Component
public class JwtUtil {

    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.secret.key}")
    private String secretKey;

    private SecretKey key;


    public String createToken(TokenType tokenType, String username, UserRole role) {
        Date now = new Date();

        if (tokenType == TokenType.ACCESS) {
            return BEARER_PREFIX + Jwts.builder()
                .expiration(new Date(now.getTime() + tokenType.getLifeTime()))
                .claim("category", tokenType.name())
                .claim("username", username)
                .claim(AUTHORIZATION_KEY, role.getAuthority())
                .issuedAt(now)
                .signWith(key)
                .compact();
        } else {
            return BEARER_PREFIX + Jwts.builder()
                .expiration(new Date(now.getTime() + tokenType.getLifeTime()))
                .claim("category", tokenType.name())
                .claim("username", username)
                .claim(AUTHORIZATION_KEY, role.getAuthority())
                .issuedAt(now)
                .signWith(key)
                .compact();
        }
    }


    @PostConstruct
    private void init() {
        // 키 설정
        key = getSecretKeyFromBase64(secretKey);
    }

    public void addTokenToHeader(HttpServletResponse response, String token) {
        token = URLEncoder.encode(token, StandardCharsets.UTF_8)
            .replaceAll("\\+", "%20");

        response.addHeader(AUTHORIZATION_HEADER, token);
    }

    public String getAccessTokenFromRequestHeader(HttpServletRequest req) {
        String header = req.getHeader(AUTHORIZATION_HEADER);
        if (header == null) {
            return null;
        }
        return getDecodeToken(header);
    }

    public String getDecodeToken(String token) {
        return URLDecoder.decode(token, StandardCharsets.UTF_8);
    }

    public String substringToken(String token) {
        if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
            return token.substring(BEARER_PREFIX.length());
        }

        log.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }

        return false;
    }

    public String getUsername(String token) {

        return getJwtParser().parseSignedClaims(token)
            .getPayload()
            .get("username", String.class);
    }

    public String getRole(String token) {

        return getJwtParser().parseSignedClaims(token)
            .getPayload()
            .get(AUTHORIZATION_KEY, String.class);
    }

    public boolean isExpired(String token) {
        return getJwtParser().parseSignedClaims(token)
            .getPayload()
            .getExpiration()
            .before(new Date());
    }

    private JwtParser getJwtParser() {
        return Jwts.parser()
            .verifyWith(key)
            .build();
    }

    private SecretKey getSecretKeyFromBase64(String base64) {
        return Keys.hmacShaKeyFor(Base64Coder.decode(base64));
    }

    public void addCookie(HttpServletResponse response, TokenType tokenType, String refreshToken) {
        refreshToken = URLEncoder.encode(refreshToken, StandardCharsets.UTF_8)
            .replaceAll("\\+", "%20");
        Cookie cookie = new Cookie(tokenType.name(), refreshToken);
        cookie.setMaxAge((int)tokenType.getLifeTime() / 1000);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public Cookie createCookie(TokenType tokenType, String refreshToken) {
        refreshToken = URLEncoder.encode(refreshToken, StandardCharsets.UTF_8)
            .replaceAll("\\+", "%20");
        Cookie cookie = new Cookie(tokenType.name(), refreshToken);
        cookie.setMaxAge((int)tokenType.getLifeTime() / 1000);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }

    public String getCategory(String token) {
        return getJwtParser().parseSignedClaims(token)
            .getPayload()
            .get("category", String.class);
    }

    public void deleteCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null); // 쿠키의 값을 null로 설정
        cookie.setMaxAge(0); // 만료 시간을 0으로 설정하여 쿠키 삭제
        cookie.setPath("/"); // 경로를 설정 (쿠키가 설정된 경로와 일치해야 함)
        response.addCookie(cookie); // 응답에 쿠키 추가
    }
}