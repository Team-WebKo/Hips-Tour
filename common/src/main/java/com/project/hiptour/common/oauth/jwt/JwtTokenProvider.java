package com.project.hiptour.common.oauth.jwt;

import com.project.hiptour.common.oauth.dto.TokenPairDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import java.security.Key;

import java.util.Date;

//@Slf4j 라는 로깅 전용 어노테이션도 있음. 나중에 조사해 보기. 설정 많아서 우선은 패스
@Component
public class JwtTokenProvider {

    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256); // 찾아봐야 함. 서버 올릴 떄마다 바뀌는지. 바뀌면 큰일
    //키 순환 및 관리 매커니즘 필요함. 외부에서 주입 받고, 여러 서버에서 동기화를 시켜줘야 함.
    private final long accessTokenValidity = 1000 * 60 * 30; //밀리초 단위. 총 30분 // yaml로 옮기기
    private final long refreshTokenValidity = 1000 * 60 * 60 * 24 * 7; // 밀리초 단위. // yaml로 옮기기 총 7일

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
