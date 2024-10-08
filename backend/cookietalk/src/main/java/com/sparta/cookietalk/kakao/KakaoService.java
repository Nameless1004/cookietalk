package com.sparta.cookietalk.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.cookietalk.auth.dto.AuthResponse;
import com.sparta.cookietalk.common.defines.Define;
import com.sparta.cookietalk.common.enums.TokenType;
import com.sparta.cookietalk.common.enums.UserRole;
import com.sparta.cookietalk.security.JwtUtil;
import com.sparta.cookietalk.user.entity.User;
import com.sparta.cookietalk.user.repository.UserRepository;
import java.net.URI;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j(topic = "KAKAO Login")
@Service
@RequiredArgsConstructor
public class KakaoService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtUtil jwtUtil;

    public AuthResponse.KakaoLogin kakaoLogin(String code) throws JsonProcessingException {
        // 인가 코드로 액세스 토큰 요청
        String accessToken = getToken(code);

        // 토큰으로 카카오 API 호출 : 액세스 토큰으로 사용자 정보 가져오기
        KakaoUserInfoDto kakaoUserInfoDto = getKakaoUserInfo(accessToken);

        User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfoDto);

        // jwt 토큰 반환
        String access = jwtUtil.createAccessToken(kakaoUser.getId(), kakaoUser.getEmail(), kakaoUser.getRole());
        String refresh = jwtUtil.createRefreshToken(kakaoUser.getId(), kakaoUser.getEmail(), kakaoUser.getRole());
        redisTemplate.opsForValue().set(Define.REDIS_REFRESH_TOKEN_KEY_PREFIX + kakaoUser.getId(), refresh, TokenType.REFRESH.getLifeTime(), TimeUnit.MILLISECONDS);
        return new AuthResponse.KakaoLogin(access, refresh);
    }

    private User registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
        Long kakaoId = kakaoUserInfo.getId();
        User kakaoUser = userRepository.findById(kakaoId).orElse(null);

        if(kakaoUser == null) {
            // 카카오 사용자 email 동일한 email 가진 회원이 있는지 확인
            String kakaoEmail = kakaoUserInfo.getEmail();
            User sameEmailUser = userRepository.findWithChannelByEmail(kakaoEmail).orElse(null);
            if(sameEmailUser != null) {
                kakaoUser = sameEmailUser;
                // 기존 회원정보에 카카오 id 추가
                kakaoUser = kakaoUser.updateKakaoId(kakaoId);
            } else {
                // 신규 회원가입
                // password: random UUID
                String password = UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(password);

                kakaoUser = new User(kakaoUserInfo.getNickname(), encodedPassword, kakaoEmail, getTempNickname(), UserRole.ROLE_USER, kakaoId);
            }

            kakaoUser = userRepository.save(kakaoUser);
        }
        return kakaoUser;
    }

    private String getTempNickname(){
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return "temp"+ uuid.substring(0,8);
    }

    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        URI uri = UriComponentsBuilder
            .fromUriString("https://kapi.kakao.com")
            .path("/v2/user/me")
            .encode()
            .build()
            .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
            .post(uri)
            .headers(headers)
            .body(new LinkedMultiValueMap<>());

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
            requestEntity,
            String.class
        );

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties").get("nickname").asText();
        String email = jsonNode.get("kakao_account")
            .get("email")
            .asText();
        log.info("카카오 사용자 정보: {} {} {}", id, nickname, email);
        return new KakaoUserInfoDto(id, nickname, email);
    }

    private String getToken(String code) throws JsonProcessingException {
        URI uri = UriComponentsBuilder.fromUriString("https://kauth.kakao.com")
            .path("/oauth/token")
            .encode()
            .build()
            .toUri();

        // HTTP header 생성
        HttpHeaders headers =  new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "fda7d84d33262e5e9a970cb237e67fb2");
        body.add("redirect_url", "http://localhost:8080/api/user/kakao/callback");
        body.add("code", code);
        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity.post(uri)
            .headers(headers)
            .body(body);

        ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

        // HTTP 응답(Json) -> 액세스 토큰 파싱
        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        return jsonNode.get("access_token").asText();
    }
}