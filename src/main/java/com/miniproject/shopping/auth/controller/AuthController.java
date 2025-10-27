package com.miniproject.auth.auth.controller;

import com.miniproject.auth.auth.dto.*;
import com.miniproject.auth.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "인증", description = "인증 관련 API")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/duplicate-check-username")
    @Operation(summary = "아이디 중복확인", description = "아이디가 중복되었는지 확인합니다.")
    public ResponseEntity<DefaultRes> checkDuplicateUsername(
            @RequestParam(name = "username") String username
    ) {
        return ResponseEntity.ok(authService.checkDuplicateUsername(username));
    }

    @PostMapping("/duplicate-check-email")
    @Operation(summary = "이메일 중복확인", description = "이메일이 중복되었는지 확인합니다.")
    public ResponseEntity<DefaultRes> checkDuplicateEmail(
            @RequestParam(name = "email") String email
    ) {
        return ResponseEntity.ok(authService.checkDuplicateEmail(email));
    }

    @PostMapping("/register")
    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다")
    public ResponseEntity<AuthRes> register(
            @RequestBody RegisterReq req
    ) {
        return ResponseEntity.ok(authService.register(req));
    }

    @PostMapping("/authenticate")
    @Operation(summary = "로그인", description = "사용자 로그인을 처리하고 access/refresh token을 발급합니다")
    public ResponseEntity<AuthRes> authenticate(
            @RequestBody AuthReq request
    ) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "access token을 블랙리스트에 추가하여 로그아웃 처리합니다")
    public ResponseEntity<DefaultRes> logout(
            @RequestBody LogoutReq req
            ) {
        authService.logout(req);
        return ResponseEntity.ok(DefaultRes.success());
    }

    @PostMapping("/refresh")
    @Operation(summary = "토큰 갱신", description = "refresh token을 사용하여 새로운 access/refresh token을 발급합니다")
    public ResponseEntity<AuthRes> refresh(
            @RequestBody RefreshTokenReq req
    ) {
        return ResponseEntity.ok(authService.refresh(req));
    }
}
