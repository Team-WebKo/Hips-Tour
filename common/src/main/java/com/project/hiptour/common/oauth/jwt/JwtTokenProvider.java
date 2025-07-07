package com.project.hiptour.common.oauth.jwt;

import com.project.hiptour.common.oauth.dto.TokenPairDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;

import java.util.Date;

@Component
public class JwtTokenProvider {

    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long accessTokenValidity = 1000 * 60 * 30; //밀리초 단위. 총 30분
    private final long refreshTokenValidity = 1000 * 60 * 60 * 24 * 7; // 밀리초 단위. 총 7일

    public String generateAccessToken(Long kakaoId){
        return Jwts.builder()
                .claim("kakaoId", kakaoId)
                .setSubject("access")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity))
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(Long kakaoId){
        return Jwts.builder()
                .claim("kakaoId", kakaoId)
                .setSubject("refresh")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidity))
                .signWith(secretKey)
                .compact();
    }

    public TokenPairDTO generateTokens(Long kakaoId){
        TokenPairDTO tokenPairDTO = new TokenPairDTO(generateAccessToken(kakaoId), generateRefreshToken(kakaoId));
        return tokenPairDTO;
    }

    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e){
            return false;
        }
    }

    private Claims parseClaimsFromToken(String token){

        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

    public Long getKakaoIdFromToken(String token){

        Claims claims = parseClaimsFromToken(token);

        if(claims != null){
            System.out.println("getKakaoId : 토큰으로부터 Claim 추출 완료");
        }

        return claims.get("kakaoId", Long.class);
    }


}
