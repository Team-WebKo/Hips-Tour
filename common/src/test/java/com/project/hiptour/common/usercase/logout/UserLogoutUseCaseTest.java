package com.project.hiptour.common.usercase.logout;

import com.project.hiptour.common.entity.users.RoleInfo;
import com.project.hiptour.common.entity.users.TokenInfo;
import com.project.hiptour.common.entity.users.UserInfo;
import com.project.hiptour.common.entity.users.UserRole;
import com.project.hiptour.common.entity.users.repos.TokenRepos;
import com.project.hiptour.common.security.OauthProviderService;
import com.project.hiptour.common.usercase.common.token.TokenPair;
import com.project.hiptour.common.usercase.services.token.TokenService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional  // 각 테스트 후 롤백
@ActiveProfiles("local")
class UserLogoutUseCaseTest {

    @MockBean
    OauthProviderService mockService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private TokenRepos tokenRepos;

    @Autowired
    UserLogoutUseCase userLogoutUseCase;

    @Test
    @DisplayName("로그아웃을 수행하면, 토큰을 모두 invalid 한다")
    void t(){

        UserInfo userInfo = UserInfo.builder()
                .userId(1L)
                .build();

        TokenPair token = tokenService.createToken(userInfo, List.of(1L));

        this.tokenService.updateRefreshTokenAfterLogin(userInfo.getUserId(), token.getRefreshToken());

        Optional<TokenInfo> tokenInfo = this.tokenRepos.findFirstByUserIdOrderByCreatedAtDesc(1L);
        assertTrue(tokenInfo.isPresent() && tokenInfo.get().isActive());

        LogoutResponse logoutResponse = this.userLogoutUseCase.logout(token.getAccessToken().getToken());

        assertTrue(logoutResponse.isSuccess());

        Optional<TokenInfo> tokenInfoAfterLogout = this.tokenRepos.findFirstByUserIdOrderByCreatedAtDesc(1L);
        assertTrue(tokenInfoAfterLogout.isPresent() && !tokenInfoAfterLogout.get().isActive());

    }
    
    @Test
    @DisplayName("로그아웃 후 사용되는 access token은 거부되어야 한다")
    void t2(){
        //TODO :: 구현필요
    }

}