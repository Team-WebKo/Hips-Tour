package com.project.hiptour.common.login.test.service;

import com.project.hiptour.common.domain.RefreshToken;
import com.project.hiptour.common.repository.JwtRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class JwtTokenService {

    private final JwtRepository jwtRepository;

    public JwtTokenService(JwtRepository jwtRepository){
        this.jwtRepository = jwtRepository;
    }

    public void saveRefreshToken(Long kakaoid, String token, LocalDateTime issuedAt, LocalDateTime expireAt){

        RefreshToken refreshToken = new RefreshToken(kakaoid, token, issuedAt, expireAt);
        jwtRepository.save(refreshToken);

    }

    public boolean isKakaoIdExists(Long kakaoid){
        return jwtRepository.existsByKakaoId(kakaoid);
    }

}
