package com.project.hiptour.common.usercase.services.token;

import com.project.hiptour.common.entity.users.UserInfo;
import com.project.hiptour.common.entity.users.repos.UserRepos;
import com.project.hiptour.common.security.OauthProviderService;
import com.project.hiptour.common.usercase.common.token.Token;
import com.project.hiptour.common.usercase.common.token.TokenPair;
import com.project.hiptour.common.usercase.common.token.TokenTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class DefaultTokenServiceImplTest {

    @MockBean
    OauthProviderService mock;

    @Autowired
    TokenService tokenService;

    @Autowired
    UserRepos userRepos;

    @Test
    @DisplayName("발행되는 active 토큰을 decode 하면, ")
    void t(){

        UserInfo userInfo = UserInfo.builder()
                .email(UUID.randomUUID().toString())
                .build();

        this.userRepos.save(userInfo);

        TokenPair token = this.tokenService.createToken(userInfo, List.of(1L));
        Token accessToken = token.getAccessToken();

        TokenTemplate tokenTemplate = this.tokenService.decodeToken(accessToken.getToken());
        assertEquals(userInfo.getUserId(), tokenTemplate.getUserId());
        assertTrue(List.of(1).containsAll(tokenTemplate.getRoleIds()));

    }


}