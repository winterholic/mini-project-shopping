package com.miniproject.auth.auth.security;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private final CacheManager cacheManager;
    private final JwtTokenProvider jwtTokenProvider;

    public void addToBlacklist(String token) {
        Cache cache = cacheManager.getCache("tokenBlacklist");
        if (cache != null) {
            Date expiration = jwtTokenProvider.getExpirationDateFromToken(token);
            cache.put(token, expiration);
        }
    }

    public boolean isBlacklisted(String token) {
        Cache cache = cacheManager.getCache("tokenBlacklist");
        if (cache != null) {
            return cache.get(token) != null;
        }
        return false;
    }
}
