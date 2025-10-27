package com.miniproject.auth.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenBlacklistService tokenBlacklistService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            String jwt = jwtTokenProvider.extractToken(request);

            if (jwt != null) {
                // 블랙리스트 체크
                if (tokenBlacklistService.isBlacklisted(jwt)) {
                    setErrorResponse(response, "블랙리스트된 토큰입니다.");
                    return;
                }

                // JWT 유효성 검증
                if (!jwtTokenProvider.validateToken(jwt)) {
                    setErrorResponse(response, "유효하지 않은 토큰입니다.");
                    return;
                }

                // JWT에서 사용자명 추출
                String username = jwtTokenProvider.getUsernameFromToken(jwt);

                // JWT에서 권한 정보 추출
                List<String> permissions = jwtTokenProvider.getPermissionsFromToken(jwt);
                List<SimpleGrantedAuthority> authorities = permissions != null
                    ? permissions.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList())
                    : List.of();

                // UserDetails 생성 (토큰의 권한 정보 사용)
                UserDetails userDetails = User.builder()
                        .username(username)
                        .password("") // 토큰 인증이므로 비밀번호 불필요
                        .authorities(authorities)
                        .build();

                // UsernamePasswordAuthenticationToken 생성
                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        authorities
                    );

                // Request 정보를 Authentication에 추가
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // SecurityContext에 인증 정보 설정
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            log.error("JWT 인증 처리 중 오류 발생: {}", e.getMessage());
            setErrorResponse(response, "인증 처리 중 오류가 발생했습니다.");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void setErrorResponse(HttpServletResponse response, String message) throws IOException {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("code", HttpStatus.UNAUTHORIZED.value());
        responseBody.put("message", message);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));

        // cors
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:8080");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "*");
    }
}
