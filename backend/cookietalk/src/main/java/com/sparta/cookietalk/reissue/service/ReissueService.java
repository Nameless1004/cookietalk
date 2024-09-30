package com.sparta.cookietalk.reissue.service;

import com.sparta.cookietalk.common.dto.ResponseDto;
import com.sparta.cookietalk.common.enums.TokenType;
import com.sparta.cookietalk.common.enums.UserRole;
import com.sparta.cookietalk.security.JwtUtil;
import com.sparta.cookietalk.user.dto.UserResponse.Reissue;
import io.jsonwebtoken.ExpiredJwtException;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReissueService {

    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, Object> redisTemplate;

    public ResponseDto<?> reissue(String refreshToken) {

        if(refreshToken == null) {
            return ResponseDto.of(HttpStatus.BAD_REQUEST, "재발급하려면 리프레쉬 토큰이 필요합니다.", null);
        }

        // 프론트에서 붙여준 Bearer prefix 제거
        try{
            refreshToken = jwtUtil.substringToken(refreshToken);
        } catch (NullPointerException e) {
            return ResponseDto.of(HttpStatus.BAD_REQUEST, "잘못된 토큰 형식 입니다.", null);
        }

        // 리프레쉬 토큰인지 검사
        String category = jwtUtil.getCategory(refreshToken);
        if (!category.equals(TokenType.REFRESH.name())) {
            return ResponseDto.of(HttpStatus.BAD_REQUEST, "리프레쉬 토큰이 아닙니다.");
        }

        // 토큰 만료 검사
        try{
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            return ResponseDto.of(HttpStatus.UNAUTHORIZED, "만료된 리프레쉬 토큰입니다.", null);
        }


        String username = jwtUtil.getUsername(refreshToken);
        // 레디스에서 리프레쉬 토큰을 가져온다.
        refreshToken = (String) redisTemplate.opsForValue().get(username);

        if (refreshToken == null) {
            return ResponseDto.of(HttpStatus.UNAUTHORIZED, "만료된 리프레쉬 토큰입니다.", null);
        }

        // 검증이 통과되었다면 refresh 토큰으로 액세스 토큰을 발행해준다.
        String role = jwtUtil.getRole(refreshToken);

        // 새 토큰 발급
        String newAccessToken = jwtUtil.createAccessToken(username, UserRole.valueOf(role), false);
        String newRefreshToken = jwtUtil.createRefreshToken(username, UserRole.valueOf(role), false);

        // TTL 새로해서
        Long ttl = redisTemplate.getExpire(username);
        redisTemplate.opsForValue().set(username, newAccessToken);
        if(ttl != null && ttl > 0) {
            redisTemplate.expire(username, ttl, TimeUnit.MILLISECONDS);
        } else {
            return ResponseDto.of(HttpStatus.UNAUTHORIZED, "만료된 리프레쉬 토큰입니다.", null);
        }

        Reissue reissue = new Reissue(newAccessToken, newRefreshToken);

        return  ResponseDto.of(HttpStatus.OK, "", reissue);
    }

}