package com.sparta.cookietalk.user.service;

import com.sparta.cookietalk.common.dto.ResponseDto;
import com.sparta.cookietalk.common.enums.UserRole;
import com.sparta.cookietalk.security.JwtUtil;
import com.sparta.cookietalk.user.dto.UserRequest;
import com.sparta.cookietalk.user.entity.User;
import com.sparta.cookietalk.user.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, Object> redisTemplate;

    // ADMIN_TOKEN
    @Value("${admin.token}")
    private String ADMIN_TOKEN;

    public void signup(UserRequest.Signup requestDto) {
        String username = requestDto.username();
        String password = passwordEncoder.encode(requestDto.password());

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findUserWithChannelByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // email 중복확인
        String email = requestDto.email();
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }

        // nickname 중복확인
        String nickname = requestDto.nickname();
        Optional<User> checkNickname = userRepository.findByNickname(nickname);
        if (checkNickname.isPresent()) {
            throw new IllegalArgumentException("중복된 닉네임 입니다.");
        }
        // 사용자 ROLE 확인
        UserRole role = UserRole.USER;
        if (requestDto.admin()) {
            if (!ADMIN_TOKEN.equals(requestDto.adminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRole.ADMIN;
        }

        // 사용자 등록
        User user = new User(username, password, email, nickname, role);
        userRepository.save(user);
    }

    public ResponseDto<?> logout(String accessToken) {
        if(accessToken == null) {
            return ResponseDto.of(HttpStatus.UNAUTHORIZED, "액세스 토큰이 없습니다.");
        }

        if(!jwtUtil.canSubstringToken(accessToken)){
            return ResponseDto.of(HttpStatus.UNAUTHORIZED, "토큰 형식이 유효하지 않습니다.");
        }

        // prefix 제거
        String token = jwtUtil.substringToken(accessToken);

        try {
            jwtUtil.isExpired(token);
        } catch (ExpiredJwtException e) {
            return ResponseDto.of(HttpStatus.UNAUTHORIZED, "만료된 액세스 토큰입니다.");
        }

        String username = jwtUtil.getUsername(token);

        // 리프레쉬 토큰 삭제
        redisTemplate.delete(username);
        return ResponseDto.of(HttpStatus.OK, "로그아웃되었습니다.");
    }
}
