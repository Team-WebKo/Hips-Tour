package com.project.hiptour.common.usercase;

import com.project.hiptour.common.usercase.services.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLogoutUseCase {
    private final TokenService tokenService;

    public void logout(Long userId) {
        tokenService.logout(userId);
    }
}
