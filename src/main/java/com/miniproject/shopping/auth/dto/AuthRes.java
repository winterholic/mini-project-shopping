package com.miniproject.auth.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRes {
    private String accessToken;
    private String refreshToken;

    public static AuthRes toDto(String accessToken, String refreshToken){
        return AuthRes.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
