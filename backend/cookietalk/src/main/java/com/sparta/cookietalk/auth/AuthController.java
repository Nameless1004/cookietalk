package com.sparta.cookietalk.auth;

import com.sparta.cookietalk.auth.dto.AuthRequest;
import com.sparta.cookietalk.auth.dto.AuthResponse;
import com.sparta.cookietalk.common.dto.ResponseDto;
import com.sparta.cookietalk.security.AuthUser;
import com.sparta.cookietalk.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/auth/nickname/check")
    public ResponseEntity<ResponseDto<AuthResponse.DuplicateCheck>> checkNickname(@RequestBody AuthRequest.CheckNickname request) {
        return ResponseDto.toEntity(HttpStatus.OK, authService.checkNickname(request));
    }
    @GetMapping("/auth/email/check")
    public ResponseEntity<ResponseDto<AuthResponse.DuplicateCheck>> checkEmail(@RequestBody AuthRequest.CheckEmail request) {
        return ResponseDto.toEntity(HttpStatus.OK, authService.checkEmail(request));
    }
    @GetMapping("/auth/username/check")
    public ResponseEntity<ResponseDto<AuthResponse.DuplicateCheck>> checkUsername(@RequestBody AuthRequest.CheckUsername request) {
        return ResponseDto.toEntity(HttpStatus.OK, authService.checkUsername(request));
    }
}
