package com.project.hiptour.common.usercase.logout;

import com.project.hiptour.common.entity.users.TokenInfo;
import com.project.hiptour.common.entity.users.repos.TokenRepos;
import com.project.hiptour.common.usercase.services.token.TokenService;
import com.project.hiptour.common.usercase.services.token.TokenTemplate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserLogoutUseCase {
    private final TokenService tokenService;
    private final TokenRepos tokenRepos;

    @Transactional
    public LogoutResponse logout(String userAccessToken) {
        try {
            TokenTemplate tokenTemplate = tokenService.decodeToken(userAccessToken);
            long userId = tokenTemplate.getUserId();
            Optional<TokenInfo> tokenInfo = this.tokenRepos.findFirstByUserIdOrderByCreatedAtDesc(userId);

            if(tokenInfo.isEmpty()){
                return LogoutResponse.builder()
                        .isSuccess(false)
                        .message("user token is not present")
                        .build();
            }

            tokenInfo.get().deactivate();
            log.debug("successfully deactivate user token");

            return LogoutResponse.builder()
                    .isSuccess(true)
                    .build();

        }catch (Exception e){
            log.warn("{}", e.getMessage());
            return LogoutResponse.builder()
                    .isSuccess(false)
                    .message(e.getMessage())
                    .build();
        }
    }
}
