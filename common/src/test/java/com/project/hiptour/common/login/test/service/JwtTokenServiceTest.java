package com.project.hiptour.common.login.test.service;

import com.project.hiptour.common.domain.RefreshToken;
import com.project.hiptour.common.repository.JwtRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class JwtTokenServiceTest {

    @Mock
    private JwtRepository jwtRepository;

    @InjectMocks
    private JwtTokenService jwtTokenService;

    @BeforeEach
    void setUp(){
        org.mockito.MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveRefreshTokenTest(){

        Long kakaoid = 123456789L;
        LocalDateTime issuedAt = LocalDateTime.now();
        LocalDateTime expireAt = issuedAt.plusDays(7);
        RefreshToken token = new RefreshToken(kakaoid, issuedAt, expireAt);

        //when -
        when(jwtRepository.save(any(RefreshToken.class))).thenReturn(token);

        jwtTokenService.saveRefreshToken(kakaoid, issuedAt, expireAt);

    }

}
