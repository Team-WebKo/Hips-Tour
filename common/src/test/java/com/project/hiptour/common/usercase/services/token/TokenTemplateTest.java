package com.project.hiptour.common.usercase.services.token;

import com.auth0.jwt.algorithms.Algorithm;
import com.project.hiptour.common.entity.users.UserInfo;
import com.project.hiptour.common.security.OauthProviderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class TokenTemplateTest {

    @Autowired
    TokenService tokenService;
    @MockBean
    OauthProviderService service;

    @Test
    @DisplayName("token 직렬화/역직렬화 테스트")
    void t(){

        UserInfo userInfo = UserInfo.builder()
                .userIdentifier("kaka")
                .nickName("ni")
                .build();

        userInfo.setUserId(1L);
        
        TokenPair token1 = this.tokenService.createToken(userInfo, List.of(1L));

        TokenTemplate tokenTemplate = this.tokenService.decodeToken(token1.getRefreshToken().getToken());

        assertEquals(1L, tokenTemplate.getUserId());
    }

}