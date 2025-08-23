package com.project.hiptour.common.oauth.controller;

import com.project.hiptour.common.oauth.dto.TokenValidateDTO;
import com.project.hiptour.common.oauth.dto.TokenPairDTO;
import com.project.hiptour.common.oauth.jwt.JwtTokenProvider;
import com.project.hiptour.common.repository.JwtRepository;
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
    private final JwtRepository jwtRepository;

    public AuthTestController(JwtTokenProvider jwtTokenProvider, JwtRepository jwtRepository){
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtRepository = jwtRepository;
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

        // 1) 기본 유효성 검사
        if (!jwtTokenProvider.validateToken(refresh)) {
            return ResponseEntity.status(401).body("invalid_or_expired_refresh");
        }

        try {
            // 2) 클레임 파싱 + subject 확인
            Claims claims = jwtTokenProvider.parseClaims(refresh);
            if (!"refresh".equals(claims.getSubject())) {
                return ResponseEntity.status(401).body("not_a_refresh_token");
            }
            Long kakaoId = claims.get("kakaoId", Long.class);

            // 3) DB의 최신 리프레시 토큰과 문자열 비교
            return jwtRepository.findTopByKakaoIdOrderByExpireatDesc(kakaoId)
                    .map(saved -> {
                        if (!saved.getToken().equals(refresh)) {
                            return ResponseEntity.status(401).body("refresh_not_latest");
                        }
                        // 4) 통과 → 새 access 발급
                        String newAccess = jwtTokenProvider.generateAccessToken(kakaoId);
                        System.out.println("[AUTH] 액세스토큰이 만료되어 새로 생성합니다. 토큰 값 : " + newAccess);
                        return ResponseEntity.ok(new TokenPairDTO(newAccess, null, false));
                    })
                    .orElseGet(() -> ResponseEntity.status(401).body("no_refresh_token_record"));

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
