package com.project.hiptour.common.oauth.jwt;

import com.project.hiptour.common.config.JwtProperties;
import com.project.hiptour.common.oauth.dto.TokenPairDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;

import java.util.Base64;
import java.util.Date;

//@Slf4j 라는 로깅 전용 어노테이션도 있음. 나중에 조사해 보기. 설정 많아서 우선은 패스
@Component
public class JwtTokenProvider {

    //키 순환 및 관리 매커니즘 필요함. 외부에서 주입 받고, 여러 서버에서 동기화를 시켜줘야 함.
    private final SecretKey secretKey;
    private final long accessTokenValidity;
    private final long refreshTokenValidity;

    public JwtTokenProvider(JwtProperties props){

        byte[] keyBytes = Decoders.BASE64.decode(props.getSecret());
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenValidity = props.getAccessValidityMs();
        this.refreshTokenValidity = props.getRefreshValidityMs();

    }

    //테스트 상황 - 토큰이 정상적으로 생성이 되는지
    public String generateAccessToken(Long kakaoId){
        return Jwts.builder()
                .claim("kakaoId", kakaoId) // 다른 값으로 교체. 의미가 없음. 보통은 유저 role, 혹은 비교대조 가능한 값
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

    public TokenPairDTO generateTokens(Long kakaoId, boolean isFirstLogin){
        TokenPairDTO tokenPairDTO = new TokenPairDTO(generateAccessToken(kakaoId), generateRefreshToken(kakaoId), isFirstLogin);
        return tokenPairDTO;
    }

    public javax.crypto.SecretKey getSecretKey() {return secretKey;}

    public io.jsonwebtoken.Claims parseClaims(String token){

        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

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
