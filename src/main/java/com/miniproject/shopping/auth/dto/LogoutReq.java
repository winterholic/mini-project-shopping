package com.miniproject.auth.auth.dto;

import lombok.Data;

@Data
public class LogoutReq {
    private String accessToken;
    private String refreshToken;

    public static void getTokenByParsing(LogoutReq req){
        req.setAccessToken(req.getAccessToken().substring(7));
        req.setRefreshToken(req.getRefreshToken().substring(7));
    }
}
