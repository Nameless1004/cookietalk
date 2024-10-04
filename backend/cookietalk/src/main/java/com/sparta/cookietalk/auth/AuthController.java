package com.sparta.cookietalk.auth;

import com.sparta.cookietalk.auth.dto.AuthRequest;
import com.sparta.cookietalk.auth.dto.AuthResponse;
import com.sparta.cookietalk.common.dto.ResponseDto;
import com.sparta.cookietalk.security.AuthUser;
import com.sparta.cookietalk.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signup")
    public ResponseEntity<ResponseDto<AuthResponse.Signup>> signup(@RequestBody AuthRequest.Signup authRequest) {
        return ResponseDto.toEntity(authService.signup(authRequest));
    }

    @PostMapping("/auth/signin")
    public ResponseEntity<ResponseDto<AuthResponse.Signin>> signin(@RequestBody AuthRequest.Signin authRequest) {
        return ResponseDto.toEntity(authService.signin(authRequest));
    }

    @PostMapping("/auth/signout")
    public ResponseEntity<?> signout(@AuthenticationPrincipal AuthUser user) {
        return ResponseDto.toEntity(authService.signout(user));
    }

    @PostMapping("/auth/reissue")
    public ResponseEntity<?> reissue(@RequestHeader(JwtUtil.REFRESH_TOKEN_HEADER) String refreshToken) {
        ResponseDto<?> reissue = authService.reissue(refreshToken);
        return ResponseDto.toEntity(reissue);
    }
}
