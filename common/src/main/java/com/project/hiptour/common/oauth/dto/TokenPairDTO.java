package com.project.hiptour.common.oauth.dto;

import lombok.Getter;

@Getter
public class TokenPairDTO {

    private final String accessToken;
    private final String refreshToken;
    private final boolean isFirstLogin;

    public TokenPairDTO(String accessToken, String refreshToken, boolean isFirstLogin){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.isFirstLogin = isFirstLogin;
    }

    public void printTokens(){
        System.out.println("Access Token : " + accessToken);
        System.out.println("Refresh Token : " + refreshToken);
        System.out.println("isFirstLogin : " + isFirstLogin);
        System.out.println("발행 중인 토큰 및 최초 로그인 여부 출력 완료");
    }

    public String getAccessToken(){
        return accessToken;
    }

    public String getRefreshToken(){
        return refreshToken;
    }

}
