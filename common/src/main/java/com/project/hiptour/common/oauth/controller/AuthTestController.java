package com.project.hiptour.common.oauth.controller;

import com.project.hiptour.common.oauth.dto.TokenValidateDTO;
import com.project.hiptour.common.oauth.dto.TokenPairDTO;
import com.project.hiptour.common.oauth.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/auth-test")
public class AuthTestController {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthTestController(JwtTokenProvider jwtTokenProvider){
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/validate-access")
    public ResponseEntity<TokenValidateDTO> validateAccessToken(@RequestBody Map<String, String> body){

        String accessToken = body.get("accessToken");
        if(accessToken == null || accessToken.isBlank()) return ResponseEntity.badRequest().build();

        boolean valid = jwtTokenProvider.validateToken(accessToken);
        String subject = null;
        Long kakaoId = null;
        long issuedAt = 0;
        long expireAt = 0;
        long now = Instant.now().getEpochSecond();

        if(valid == true) {
            Claims c = jwtTokenProvider.parseClaims(accessToken);
            subject = c.getSubject();
            kakaoId = c.get("kakaoId", Long.class);
            issuedAt = c.getIssuedAt().toInstant().getEpochSecond();
            expireAt = c.getExpiration().toInstant().getEpochSecond();

        } else{

        }

        long remainSecond = 0;

        if(expireAt == 0) remainSecond = -1;
        else remainSecond = expireAt - now;

        return ResponseEntity.ok(new TokenValidateDTO(valid, subject, kakaoId, issuedAt, expireAt, remainSecond));

    }

    @PostMapping("/validate-refresh")
    public ResponseEntity<TokenValidateDTO> introspectRefresh(@RequestBody Map<String, String> body) {
        String refresh = body.get("refreshToken");
        if (refresh == null || refresh.isBlank()) return ResponseEntity.badRequest().build();

        boolean valid = jwtTokenProvider.validateToken(refresh);
        String subject = null;
        Long kakaoId = null;
        long iat = 0, exp = 0, now = Instant.now().getEpochSecond();

        if (valid) {
            Claims c = jwtTokenProvider.parseClaims(refresh);
            subject = c.getSubject();
            kakaoId = c.get("kakaoId", Long.class);
            iat = c.getIssuedAt().toInstant().getEpochSecond();
            exp = c.getExpiration().toInstant().getEpochSecond();
        }

        long remaining = (exp == 0 ? -1 : exp - now);
        return ResponseEntity.ok(new TokenValidateDTO(valid, subject, kakaoId, iat, exp, remaining));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> body) {
        String refresh = body.get("refreshToken");
        if (refresh == null || refresh.isBlank()) {
            return ResponseEntity.badRequest().body("refreshToken is required");
        }

        if (!jwtTokenProvider.validateToken(refresh)) {
            return ResponseEntity.status(401).body("invalid_or_expired_refresh");
        }

        try {
            SecretKey key = jwtTokenProvider.getSecretKey();
            Claims claims = jwtTokenProvider.parseClaims(refresh); // 동일 키로 파싱
            if (!"refresh".equals(claims.getSubject())) {
                return ResponseEntity.status(401).body("not_a_refresh_token");
            }
            Long kakaoId = claims.get("kakaoId", Long.class);
            String newAccess = jwtTokenProvider.generateAccessToken(kakaoId);

            System.out.println("액세스토큰이 만료되어 새로 생성합니다. 토큰 값 : " + newAccess);

            // 재발급 단계에선 access만 반환 (refresh 회전은 다음 단계에서)
            return ResponseEntity.ok(new TokenPairDTO(newAccess, null, false));

        } catch (JwtException e) {
            return ResponseEntity.status(401).body("invalid_refresh_token");
        }
    }

    @GetMapping("/protected-echo")
    public ResponseEntity<String> protectedEcho(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("missing_bearer");
        }
        String access = authHeader.substring(7);
        if (!jwtTokenProvider.validateToken(access)) {
            return ResponseEntity.status(401).body("invalid_or_expired_access");
        }
        Long kakaoId = jwtTokenProvider.getKakaoIdFromToken(access);
        return ResponseEntity.ok("hello user " + kakaoId);
    }


}
