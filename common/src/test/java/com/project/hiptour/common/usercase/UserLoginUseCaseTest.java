package com.project.hiptour.common.usercase;

import com.project.hiptour.common.entity.users.TokenInfo;
import com.project.hiptour.common.entity.users.UserInfo;
import com.project.hiptour.common.security.OauthProviderService;
import com.project.hiptour.common.security.UserIdentity;
import com.project.hiptour.common.security.kakao.KakaoUserIdentity;
import com.project.hiptour.common.usercase.services.login.UserService;
import com.project.hiptour.common.usercase.services.token.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("local")
class UserLoginUseCaseTest {

    @Autowired
    UserLoginUseCase userLoginUseCase;
    @Autowired
    UserService userService;
    @Autowired
    TokenService tokenService;
    @MockBean
    OauthProviderService mockService;

    @DisplayName("새로운 유저가 생성되면, 토큰과 함께, 새로운 유저라는 플래그가 추가된 결과를 반환한다.")
    @Test
    void t1(){

        UserIdentity identity = new KakaoUserIdentity(10);

        when(mockService.getUserIdentity(anyString())).thenReturn(identity);

        LoginResult loginResult = this.userLoginUseCase.createTokenPair(identity.getUserIdentifier());

        assertNotNull(loginResult);
        assertTrue(loginResult.isNewSingUp());

        TokenPair pair = loginResult.getPair();
        assertNotNull(pair);

        Token accessToken = pair.getAccessToken();
        assertNotNull(accessToken);

    }

    @Test
    @DisplayName("새로운 유저가 생성되면, 유저 정보를 저장한다.")
    void t2(){

        UserIdentity identity = new KakaoUserIdentity(10);

        when(mockService.getUserIdentity(anyString())).thenReturn(identity);

        Optional<UserInfo> userInfoByIdentifier = this.userService.findUserInfoByIdentifier(identity.getUserIdentifier());
        assertTrue(userInfoByIdentifier.isPresent());

    }

    @Test
    @DisplayName("유저가 로그인하면, 리프레시 토큰을 저장, 관리한다.")
    void t3(){

        UserIdentity identity = new KakaoUserIdentity(10);

        when(mockService.getUserIdentity(anyString())).thenReturn(identity);
        this.userLoginUseCase.createTokenPair(identity.getUserIdentifier());

        TokenInfo tokenInfo = this.tokenService.findByUserId(10);

        assertNotNull(tokenInfo);

    }

}