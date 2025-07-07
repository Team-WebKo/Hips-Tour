package com.project.hiptour.common.login.test.controller;

import com.project.hiptour.common.domain.UserTest;
import com.project.hiptour.common.oauth.jwt.JwtTokenProvider;
import com.project.hiptour.common.oauth.dto.TokenPairDTO;
import com.project.hiptour.common.repository.UserTestRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserTestController {

    private final UserTestRepository userTestRepository;
    private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();

    public UserTestController(UserTestRepository userTestRepository){
        this.userTestRepository = userTestRepository;
    }

    @GetMapping("/user")
    public ResponseEntity<OAuth2User> user(OAuth2AuthenticationToken token){

        OAuth2User oauth2User = token.getPrincipal();
        Map<String, Object> tokenAttributes = oauth2User.getAttributes();

        Long oauthName = Long.valueOf(tokenAttributes.get("id").toString());
        String nickname = ((Map<String, Object>)tokenAttributes.get("properties")).get("nickname").toString();

        if(userTestRepository.existsByOauthName(oauthName) == true){
            System.out.println("로그인 한 적이 있는 카카오 사용자입니다.");
        } else {
            System.out.println("처음 접속한 사용자입니다. DB에 정보를 저장합니다.");
            UserTest usertest = new UserTest(oauthName, nickname);
            userTestRepository.save(usertest);
        }

        TokenPairDTO tokenPairDTO = jwtTokenProvider.generateTokens(oauthName);
        tokenPairDTO.printTokens();

        if(jwtTokenProvider.validateToken(tokenPairDTO.getAccessToken()) == true){
            System.out.println("Access토큰 유효성 검사(대충) : true");
        } else {
            System.out.println("Access토큰 유효성 검사(대충) : false");
        }

        if(jwtTokenProvider.validateToken(tokenPairDTO.getRefreshToken()) == true){
            System.out.println("Refresh토큰 유효성 검사(대충) : true");
        } else{
            System.out.println("Refresh토큰 유효성 검사(대충) : false");
        }

        //System.out.println("토큰으로부터 추출한 카카오 id : " + oauthName);
        //System.out.println("토큰으로부터 추출한 카카오 닉네임 : " + nickname);
        System.out.println("Attributes : " + oauth2User.getAttributes());
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + tokenPairDTO.getAccessToken())
                .body(oauth2User);
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

    @GetMapping("/tokenvalidate")
    public ResponseEntity<String> validateTokenFromHeader(@RequestHeader("Authorization") String authHeader){

            if(authHeader == null){
                System.out.println("/tokenvalidate : 현재 Authorization 헤더가 존재하지 않습니다.");
                return ResponseEntity.badRequest().body("Authorization 헤더가 존재하지 않습니다.");
            }

            String token = authHeader.replace("Bearer ", "");
            boolean isTokenValid = jwtTokenProvider.validateToken(token);

            if(isTokenValid == true){
                return ResponseEntity.ok("발행된 토큰은 현재 유효합니다.");
            } else {
                return ResponseEntity.ok("토큰이 만료되었습니다.");
            }

    }

}