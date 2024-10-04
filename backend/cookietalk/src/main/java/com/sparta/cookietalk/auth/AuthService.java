package com.sparta.cookietalk.auth;

import com.sparta.cookietalk.auth.dto.AuthRequest;
import com.sparta.cookietalk.auth.dto.AuthResponse;
import com.sparta.cookietalk.common.defines.Define;
import com.sparta.cookietalk.common.dto.ResponseDto;
import com.sparta.cookietalk.common.enums.TokenType;
import com.sparta.cookietalk.common.enums.UserRole;
import com.sparta.cookietalk.common.exceptions.InvalidRequestException;
import com.sparta.cookietalk.security.AuthUser;
import com.sparta.cookietalk.security.JwtUtil;
import com.sparta.cookietalk.user.entity.User;
import com.sparta.cookietalk.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> redisTemplate;
    /**
     * 회원가입
     * @param request
     * @return
     */
    public ResponseDto<AuthResponse.Signup> signup(AuthRequest.Signup request) {
        String username = request.username();
        String password = passwordEncoder.encode(request.password());

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new InvalidRequestException("중복된 사용자가 존재합니다.");
        }

        // email 중복확인
        String email = request.email();
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new InvalidRequestException("중복된 Email 입니다.");
        }

        // nickname 중복확인
        String nickname = request.nickname();
        Optional<User> checkNickname = userRepository.findByNickname(nickname);
        if (checkNickname.isPresent()) {
            throw new InvalidRequestException("중복된 닉네임 입니다.");
        }

        // 사용자 등록
        User user = new User(username, password, email, nickname, request.userRole());
        userRepository.save(user);

        return ResponseDto.of(HttpStatus.CREATED, "성공적으로 회원가입 되었습니다.", new AuthResponse.Signup(user));
    }

    /**
     * 로그인
     * @param request
     * @return
     */
    public ResponseDto<AuthResponse.Signin> signin(AuthRequest.Signin request) {
        User user = userRepository.findByUsername(request.username()).orElseThrow(()-> new InvalidRequestException("아이디 또는 비밀번호가 잘못되었습니다."));

        if(!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidRequestException("아이디 또는 비밀번호가 잘못되었습니다.");
        }

        String accessToken = jwtUtil.createAccessToken(user.getId(), user.getEmail(), user.getRole());
        String refreshToken = jwtUtil.createRefreshToken(user.getId(), user.getEmail(), user.getRole());

        redisTemplate.opsForValue().set(Define.REDIS_REFRESH_TOKEN_KEY_PREFIX + user.getId(), refreshToken, TokenType.REFRESH.getLifeTime(), TimeUnit.MILLISECONDS);
        return ResponseDto.of(HttpStatus.OK, new AuthResponse.Signin(user, accessToken, refreshToken));
    }

    /**
     * 로그아웃
     * @param user
     * @return
     */
    public ResponseDto<?> signout(AuthUser user) {
        redisTemplate.delete(String.valueOf(user.getUserId()));
        return ResponseDto.of(HttpStatus.OK, "로그아웃되었습니다.");
    }

    /**
     * 액세스, 리프레쉬 토큰 재발행
     * @param refreshToken
     * @return
     */
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


        String key = Define.REDIS_REFRESH_TOKEN_KEY_PREFIX + jwtUtil.getUserId(refreshToken);
        // 레디스에서 리프레쉬 토큰을 가져온다.
        refreshToken = (String) redisTemplate.opsForValue().get(key);

        if (refreshToken == null) {
            return ResponseDto.of(HttpStatus.UNAUTHORIZED, "만료된 리프레쉬 토큰입니다.", null);
        }

        // redis에서 꺼내온 리프레쉬 토큰 prefix 제거
        refreshToken = jwtUtil.substringToken(refreshToken);

        // 검증이 통과되었다면 refresh 토큰으로 액세스 토큰을 발행해준다.
        Claims claims = jwtUtil.extractClaims(refreshToken);
        Long userId = Long.parseLong(claims.getSubject());
        String email = claims.get("email", String.class);
        UserRole userRole = UserRole.of(claims.get("userRole", String.class));

        // 새 토큰 발급
        String newAccessToken = jwtUtil.createAccessToken(userId, email, userRole);
        String newRefreshToken = jwtUtil.createRefreshToken(userId, email, userRole);

        // TTL 새로해서
        String userIdToString = String.valueOf(userId);
        Long ttl = redisTemplate.getExpire(userIdToString);
        redisTemplate.opsForValue().set(Define.REDIS_REFRESH_TOKEN_KEY_PREFIX + userIdToString, newAccessToken);

        if(ttl != null && ttl > 0) {
            redisTemplate.expire(userIdToString, ttl, TimeUnit.MILLISECONDS);
        } else {
            return ResponseDto.of(HttpStatus.UNAUTHORIZED, "만료된 리프레쉬 토큰입니다.", null);
        }

        AuthResponse.Reissue reissue = new AuthResponse.Reissue(newAccessToken, newRefreshToken);

        return  ResponseDto.of(HttpStatus.OK, "", reissue);
    }
}
