package com.project.hiptour.common.usercase.token;

import com.project.hiptour.common.entity.users.TokenInfo;
import com.project.hiptour.common.entity.users.UserInfo;
import com.project.hiptour.common.entity.users.UserRole;
import com.project.hiptour.common.entity.users.repos.TokenRepos;
import com.project.hiptour.common.entity.users.repos.UserRepos;
import com.project.hiptour.common.entity.users.repos.UserRoleRepo;
import com.project.hiptour.common.usercase.services.token.TokenPair;
import com.project.hiptour.common.usercase.services.token.TokenService;
import com.project.hiptour.common.usercase.services.token.TokenTemplate;
import com.project.hiptour.common.web.auth.token.TokenResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class TokenUseCase {

    private final TokenService tokenService;
    private final TokenRepos tokenRepos;
    private final UserRepos userRepos;
    private final UserRoleRepo userRoleRepo;

    @Transactional
    public TokenRequestResult validateRefreshToken(String tokenString){

        TokenTemplate tokenTemplate = tokenService.decodeToken(tokenString);
        if(tokenTemplate == null){
            log.warn("this tokenString request is invalid");
            return new TokenRequestResult(false, "this tokenString request is invalid",null);
        }

        long userId = tokenTemplate.getUserId();
        Optional<TokenInfo> token = this.tokenRepos.findFirstByUserIdOrderByCreatedAtDesc(userId);

        if(token.isPresent()&& token.get().isStillAvailable(LocalDateTime.now())){
            log.debug("this refresh tokenString is still available");
            Optional<UserInfo> userInfo = this.userRepos.findById(userId);
            if(userInfo.isEmpty()){
                return new TokenRequestResult(false, "user do not exist",null);
            }

            UserInfo userInfoObject = userInfo.get();
            List<Long> userRoleIds = userRoleRepo.findByUserInfo(userInfoObject)
                    .orElse(List.of())
                    .stream()
                    .map(UserRole::getUserRoleId).toList();

            TokenPair newToken = this.tokenService.createToken(userInfoObject, userRoleIds);

            return new TokenRequestResult(true, "success",newToken.getAccessToken().getToken());

        }else{
            return new TokenRequestResult(false, "failed",null);
        }
    }
}
