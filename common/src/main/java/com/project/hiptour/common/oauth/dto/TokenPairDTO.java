package com.project.hiptour.common.oauth.dto;

import lombok.Getter;

@Getter
public class TokenPairDTO {

    private final String accessToken;
    private final String refreshToken;

    public TokenPairDTO(String accessToken, String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public void printTokens(){
        System.out.println(accessToken);
        System.out.println(refreshToken);
    }

}
