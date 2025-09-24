package com.project.hiptour.common.usercase;

import com.project.hiptour.common.entity.users.TokenInfo;
import com.project.hiptour.common.entity.users.UserInfo;
import com.project.hiptour.common.entity.users.repos.TokenRepos;
import com.project.hiptour.common.entity.users.repos.UserRepos;
import com.project.hiptour.common.security.OauthProviderService;
import com.project.hiptour.common.security.UserIdentity;
import com.project.hiptour.common.security.kakao.KakaoUserIdentity;
import com.project.hiptour.common.usercase.common.token.Token;
import com.project.hiptour.common.usercase.common.token.TokenPair;
import com.project.hiptour.common.usercase.login.LoginResult;
import com.project.hiptour.common.usercase.login.UserLoginUseCase;
import com.project.hiptour.common.usercase.services.login.UserService;
import com.project.hiptour.common.usercase.services.token.TokenService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional  // 각 테스트 후 롤백
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
    @Autowired
    UserRepos userRepos;
    @Autowired
    TokenRepos tokenRepos;

    @DisplayName("새로운 유저가 생성되면, 토큰과 함께, 새로운 유저라는 플래그가 추가된 결과를 반환한다.")
    @Test
    void t1(){

        UserIdentity identity = new KakaoUserIdentity(new Random(100).nextInt());

        when(mockService.getUserIdentity(identity.getUserIdentifier())).thenReturn(identity);

        LoginResult loginResult = this.userLoginUseCase.requestLoginByOAuth(identity.getUserIdentifier());

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

        UserIdentity identity = new KakaoUserIdentity(new Random().nextInt());

        when(mockService.getUserIdentity(anyString())).thenReturn(identity);

        this.userService.insertNewUserAndGet(identity);

        Optional<UserInfo> userInfoByIdentifier = this.userRepos.findByUserIdentifier(identity.getUserIdentifier());
        assertTrue(userInfoByIdentifier.isPresent());

    }

    @Test
    @DisplayName("유저가 로그인하면, 리프레시 토큰을 저장, 관리한다.")
    void t3(){

        int userId = new Random().nextInt();
        UserIdentity identity = new KakaoUserIdentity(userId);

        when(mockService.getUserIdentity(anyString())).thenReturn(identity);
        this.userLoginUseCase.requestLoginByOAuth(identity.getUserIdentifier());

        List<TokenInfo> allTokenInfo = this.tokenRepos.findAll();
        assertEquals(1, allTokenInfo.size());

    }

    @Test
    @DisplayName("유저가 로그인 후, 중복으로 또 로그인을 시도하면, 기존에 등록된 리프레시 토큰을 disable 처리한다.")
    void t34(){

        int userId = new Random().nextInt();
        UserIdentity identity = new KakaoUserIdentity(userId);

        when(mockService.getUserIdentity(anyString())).thenReturn(identity);
        when(mockService.getUserIdentity(anyString())).thenReturn(identity);

        this.userLoginUseCase.requestLoginByOAuth(identity.getUserIdentifier());
        this.userLoginUseCase.requestLoginByOAuth(identity.getUserIdentifier());

        List<TokenInfo> allTokenInfo = this.tokenRepos.findAll(Sort.by(Sort.Order.by("createdAt")));
        assertEquals(2, allTokenInfo.size());
        assertFalse(allTokenInfo.get(0).isActive());
        assertTrue(allTokenInfo.get(1).isActive());

    }

}