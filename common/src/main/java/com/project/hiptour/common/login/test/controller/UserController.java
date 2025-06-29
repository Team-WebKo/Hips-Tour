package com.project.hiptour.common.login.test.controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserController {

    @GetMapping("/user")
    public OAuth2User user(OAuth2AuthenticationToken token){
        OAuth2User oauth2User = token.getPrincipal();
        System.out.println("Attributes : " + oauth2User.getAttributes());
        return oauth2User;
    }

//    public OAuth2User user(Principal principal){
//        System.out.println("Attributes : " + principal.getAtt);
//        return principal.getPri;
//    }

    //리다이렉트 URL. 카카오 로그인 - 인가 코드 발급 + 토큰 받기까지 완료되면 이 URL로 이동합니다.
    @GetMapping("/login/oauth2/code/kakao")
    public String result(){
        System.out.println("액세스 토큰 발급 완료");
        return "액세스 토큰 발급 완료";
    }

    @GetMapping("/")
    public String homeresult(){
        System.out.println("베이스 URL로 이동했음");
        return "베이스 URL로 이동했음";
    }

    @GetMapping("/token")
    public String nowTokenPrint(OAuth2AuthenticationToken token){
        if(token != null){
        System.out.println("현재 토큰 정보 : " + token);
        return "현재 토큰 정보 확인 중";
        } else return "현재 발행된 토큰이 없습니다. 로그인을 먼저 진행해 주세요.";
    }



}