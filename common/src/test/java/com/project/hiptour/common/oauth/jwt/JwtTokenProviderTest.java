package com.project.hiptour.common.oauth.jwt;

import com.project.hiptour.common.oauth.dto.TokenPairDTO;
import org.h2.command.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import com.project.hiptour.common.oauth.jwt.JwtTokenProvider;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;



public class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();

    @Test
    void AccessToken_및_RefreshToken_생성_테스트(){

        Long kakaoId = 123456789L;

        TokenPairDTO tokenPairDTO = jwtTokenProvider.generateTokens(kakaoId, true);

        assertThat(tokenPairDTO.getAccessToken()).isNotEmpty();
        assertThat(tokenPairDTO.getRefreshToken()).isNotEmpty();

    }

    @Test
    void AccessToken_및_RefreshToken_유효성_검사(){

        Long kakaoId = 123456789L;
        TokenPairDTO tokenPairDTO = jwtTokenProvider.generateTokens(kakaoId, true);

        assertThat(jwtTokenProvider.validateToken(tokenPairDTO.getAccessToken())).isTrue();
        assertThat(jwtTokenProvider.validateToken(tokenPairDTO.getRefreshToken())).isTrue();

    }

    @Test
    void AccessToken으로부터_kakaoId_추출(){

        Long kakaoId = 123456789L;

        String accessToken = jwtTokenProvider.generateAccessToken(kakaoId);

        Long compareId = jwtTokenProvider.getKakaoIdFromToken(accessToken);

        assertThat(compareId).isEqualTo(kakaoId);
    }


}
