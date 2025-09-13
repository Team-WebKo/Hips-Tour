package com.project.hiptour.common.web.auth.login;

import com.project.hiptour.common.security.OauthProviderService;
import com.project.hiptour.common.usercase.login.LoginResult;
import com.project.hiptour.common.usercase.login.UserLoginUseCase;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/login")
@AllArgsConstructor
@Slf4j
public class LoginController {
    //TODO :: provider가 복수개 존재하는 경우에 대해서 처리해야 함. 현재는 카카오만 처리가능한 구조
    private final OauthProviderService providerService;
    private final UserLoginUseCase loginUseCase;

    @GetMapping("/kakao")
    public void redirectToKakao(HttpServletResponse response) throws IOException {
        //TODO :: 로그인이 된 상태에서 반복적으로 로그인 시도를 하는 것을 막는 로직이 있으면 좋을 듯!!
        log.info("request for this url came");
        String redirectUrl =providerService.getRedirectUrl();
        response.sendRedirect(redirectUrl);
    }

    @GetMapping("/oauth2/code/kakao")
    public ResponseEntity<LoginReqResponse> handleAuthentication(@RequestParam String code){

        LoginResult res = this.loginUseCase.createTokenPair(code);;
        LoginReqResponse response = LoginReqResponse.builder()
                .isSignUpNeeded(res.isNewSingUp())
                .accessToken(res.getPair().getAccessToken().getToken())
                .refreshToken(res.getPair().getRefreshToken().getToken())
                .build();
        //TODO :: 예외처리 추가 필요

        return ResponseEntity.ok(response);
    }
}
