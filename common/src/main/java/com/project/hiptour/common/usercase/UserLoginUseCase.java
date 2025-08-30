package com.project.hiptour.common.usercase;

import com.project.hiptour.common.entity.users.UserInfo;
import com.project.hiptour.common.security.OauthProviderService;
import com.project.hiptour.common.security.UserIdentity;
import com.project.hiptour.common.usercase.services.login.UserService;
import com.project.hiptour.common.usercase.services.token.TokenPair;
import com.project.hiptour.common.usercase.services.token.TokenService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
public class UserLoginUseCase {

    private final TokenService tokenService;
    private final UserService userService;
    private final OauthProviderService providerService;

    /**
     * user의 Oauth 로그인에 따라 토큰을 생성한다 - access token, refresh token
     * @param userCode 사용자가 auth provider의 auth server로부터 인증 후 받은 코드 정보
     * @apiNote <p>
     *     auth provider로부터 받아온 인증정보를 이용해 인증 정보를 생성한 이후, 이를 기반으로 회원가입 여부를 확인한다.
     *     만약, 최초 회원가입이라면 회원 정보를 기입한 이후, 응답 결과에 최초 회원 가입 여부를 표시하고, 추가적인 정보를 요청할 수도 있다.
     *     또한, 로그인 시, refresh token 정보가 만약 남아있다면, 이를 invalidate 한 이후, 새로운 refresh token 정보를
     *     데이터베이스에 저장하도록 한다. 이를 통해, refresh token이 남용되는 것을 피할 수 있도록 한다.
     * </p>
     * **/
    @Transactional
    public LoginResult createTokenPair(String userCode){

        UserIdentity userIdentity = this.providerService.getUserIdentity(userCode);

        Optional<UserInfo> userInfoByIdentifier = userService.findUserInfoByIdentifier(userIdentity.getUserIdentifier());

        TokenPair pair;

        boolean isThisUserAlreadyExisting = false;

        if(userInfoByIdentifier.isEmpty()){
            //TODO :: 유저의 정보를 로그로 남기는 것은 피해야함! 우선은 두고, 추후 변경
            log.debug("this user {} does not exists", userIdentity.getUserIdentifier());
            UserInfo userInfo = this.userService.insertNewUserAndGet(userIdentity);
            pair = this.tokenService.createToken(userInfo);
            isThisUserAlreadyExisting = true;

        }else{
            pair = this.tokenService.createToken(userInfoByIdentifier.get());
        }

        this.tokenService.updateToken(userIdentity.getUserId(), pair.getRefreshToken());
        log.debug("token successfully updated!");

        return LoginResult.builder()
                .isNewSingUp(isThisUserAlreadyExisting)
                .pair(pair)
                .build();

    };
}
