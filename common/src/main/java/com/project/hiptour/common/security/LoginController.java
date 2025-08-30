package com.project.hiptour.common.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/login")
@AllArgsConstructor
public class LoginController {

    private final OauthProviderService providerService;

    @GetMapping("/kakao")
    public void redirectToKakao(HttpServletResponse response) throws IOException {
        String redirectUrl =providerService.getRedirectUrl();
        response.sendRedirect(redirectUrl);
    }

    @GetMapping("/oauth2/code/kakao")
    public Map<String,Object> handleAuthentication(@RequestParam String code){
        UserIdentity userIdentity = this.providerService.getUserIdentity(code);
        System.out.println(userIdentity);
        return Map.of();
    }
}
