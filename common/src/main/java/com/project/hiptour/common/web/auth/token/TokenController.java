package com.project.hiptour.common.web.auth.token;

import com.project.hiptour.common.entity.users.TokenInfo;
import com.project.hiptour.common.entity.users.UserInfo;
import com.project.hiptour.common.entity.users.repos.UserRepos;
import com.project.hiptour.common.usercase.services.token.TokenPair;
import com.project.hiptour.common.usercase.services.token.TokenService;
import com.project.hiptour.common.usercase.services.token.TokenTemplate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/token")
@Slf4j
@AllArgsConstructor
public class TokenController {

    private final TokenService tokenService;
    private final UserRepos userRepos;
    /**
     * @apiNote refresh token 유효성 검사를 수행하고, 성공하면 새로운 access token을 발급한다
     * **/

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<TokenResponse> verifyAndReturnAccessToken(@RequestBody TokenRequest request){

        log.debug("refresh token request");

        String requestRefreshToken = request.getRefreshToken();
        TokenTemplate tokenTemplate = tokenService.decodeToken(requestRefreshToken);
        if(tokenTemplate == null){
            log.warn("this token request is invalid");
            return ResponseEntity.badRequest().body(
                new TokenResponse("invalid token","")
            );
        }

        long userId = tokenTemplate.getUserId();
        TokenInfo tokenInfo = this.tokenService.findByUserId(userId);

        if(tokenInfo != null && tokenInfo.isStillAvailable(LocalDateTime.now())){
            log.debug("this refresh token is still available");
            Optional<UserInfo> userInfo = this.userRepos.findById(userId);
            if(userInfo.isEmpty()){
                return ResponseEntity.badRequest().body(
                        new TokenResponse("user is not present","")
                );
            }

            TokenPair token = this.tokenService.createToken(userInfo.get());

            return ResponseEntity.ok(
                    new TokenResponse("success", token.getAccessToken().getToken())
            );

        }

        return ResponseEntity.badRequest().body(
                new TokenResponse("request failed","")
        );

    }

}
