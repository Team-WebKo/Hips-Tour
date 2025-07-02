package com.project.hiptour.common.oauth;

import com.project.hiptour.common.oauth.dto.TokenPairDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;

import java.util.Date;

@Component
public class JwtTokenProvider {

    private final String secretKey = "secret-key-for-test";
    private final long accessTokenValidity = 1000 * 60 * 30; //밀리초 단위. 총 30분
    private final long refreshTokenValidity = 1000 * 60 * 60 * 24 * 7; // 밀리초 단위. 총 7일

    public String generateAccessToken(Long kakaoId){
        return Jwts.builder()
                .claim("kakaoId", kakaoId)
                .setSubject("access")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity))
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }

    public String generateRefreshToken(Long kakaoId){
        return Jwts.builder()
                .claim("kakaoId", kakaoId)
                .setSubject("refresh")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidity))
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();

    }

    public TokenPairDTO generateTokens(Long kakaoId){
        TokenPairDTO tokenPairDTO = new TokenPairDTO(generateAccessToken(kakaoId), generateRefreshToken(kakaoId));
        return tokenPairDTO;
    }


}
