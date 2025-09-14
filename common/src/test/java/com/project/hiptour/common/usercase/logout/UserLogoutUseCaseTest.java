package com.project.hiptour.common.usercase.logout;

import com.project.hiptour.common.usercase.UserLogoutUseCase;
import com.project.hiptour.common.usercase.services.token.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserLogoutUseCaseTest {
    @InjectMocks
    private UserLogoutUseCase userLogoutUseCase;

    @Mock
    private TokenService tokenService;

    @Test
    @DisplayName("로그아웃 시 TokenService의 logout 메소드를 호출해야 한다.")
    void call_logout_on_token_service() {
        Long userId = 1L;

        userLogoutUseCase.logout(userId);

        verify(tokenService, times(1)).logout(userId);
    }
}
