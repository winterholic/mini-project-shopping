package com.miniproject.auth.auth.service;

import com.miniproject.auth.auth.dto.*;
import com.miniproject.auth.auth.security.TokenBlacklistService;
import com.miniproject.auth.common.entity.Role;
import com.miniproject.auth.common.entity.User;
import com.miniproject.auth.common.exceiption.exceptions.InvalidAuthException;
import com.miniproject.auth.common.exceiption.exceptions.NotFoundEntityException;
import com.miniproject.auth.common.repository.RoleRepository;
import com.miniproject.auth.common.repository.UserRepository;
import com.miniproject.auth.auth.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenBlacklistService tokenBlacklistService;
    private final RoleRepository roleRepository;

    public DefaultRes checkDuplicateUsername(String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            return DefaultRes.checkDuplicateFail("아이디가 중복되었습니다.");
        }
        return DefaultRes.checkDuplicateSuccess();
    }

    public DefaultRes checkDuplicateEmail(String email){
        if (userRepository.findByEmail(email).isPresent()){
            return DefaultRes.checkDuplicateFail("이메일이 중복되었습니다.");
        }

        return DefaultRes.checkDuplicateSuccess();
    }

    public AuthRes register(RegisterReq req) {
        req.setPassword(passwordEncoder.encode(req.getPassword()));
        Role role = roleRepository.findById(4).orElseThrow(NotFoundEntityException::new);

        User user = req.toEntity(role);

        userRepository.save(user);

        List<String> permissions = extractPermissions(user);
        String accessToken = jwtTokenProvider.generateAccessToken(user.getUsername(), permissions);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());
        return AuthRes.toDto(accessToken, refreshToken);
    }

    public AuthRes authenticate(AuthReq req) {
        User user = (User) userRepository.findByUsername(req.getUsername())
                .orElseThrow(NotFoundEntityException::new);

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new InvalidAuthException("패스워드가 일치하지 않습니다.");
        }

        List<String> permissions = extractPermissions(user);
        String accessToken = jwtTokenProvider.generateAccessToken(user.getUsername(), permissions);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());
        return AuthRes.toDto(accessToken, refreshToken);
    }

    public DefaultRes logout(LogoutReq req) {
        LogoutReq.getTokenByParsing(req);
        tokenBlacklistService.addToBlacklist(req.getAccessToken());
        tokenBlacklistService.addToBlacklist(req.getRefreshToken());
        return DefaultRes.success();
    }

    public AuthRes refresh(RefreshTokenReq req) {
        req.setRefreshToken(req.getRefreshToken().substring(7));
        if (!jwtTokenProvider.validateToken(req.getRefreshToken())) {
            throw new InvalidAuthException("토큰이 올바르지 않습니다.");
        }

        String tokenType = jwtTokenProvider.getTokenType(req.getRefreshToken());
        if (!"refresh".equals(tokenType)) {
            throw new InvalidAuthException("리프레시 토큰이 아닙니다.");
        }

        String username = jwtTokenProvider.getUsernameFromToken(req.getRefreshToken());
        User user = (User) userRepository.findByUsername(username)
                .orElseThrow(NotFoundEntityException::new);

        List<String> permissions = extractPermissions(user);
        String newAccessToken = jwtTokenProvider.generateAccessToken(user.getUsername(), permissions);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());

        tokenBlacklistService.addToBlacklist(req.getRefreshToken());

        return AuthRes.toDto(newAccessToken, newRefreshToken);
    }

    private List<String> extractPermissions(User user) {
        if (user.getRole() == null) {
            return new ArrayList<>();
        }

        Role role = user.getRole();
        return role.getPermissions().stream()
                .map(rolePermission -> rolePermission.getPermission().getName())
                .collect(Collectors.toList());
    }
}
