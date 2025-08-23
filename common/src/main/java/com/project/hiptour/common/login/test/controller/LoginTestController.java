package com.project.hiptour.common.login.test.controller;

import com.project.hiptour.common.domain.RefreshToken;
import com.project.hiptour.common.domain.UserTest;
import com.project.hiptour.common.oauth.jwt.JwtTokenProvider;
import com.project.hiptour.common.oauth.dto.TokenPairDTO;
import com.project.hiptour.common.repository.JwtRepository;
import com.project.hiptour.common.repository.UserTestRepository;
import io.jsonwebtoken.Claims;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.util.Map;

@RestController
public class LoginTestController {

    private final UserTestRepository userTestRepository;
    private final JwtRepository jwtRepository;
    //아래처럼 new 하지 말고 bean 객체로 주입하기 + 방법 찾아보기. 테스트할 때 bean 객체가 있어야 가능하다고 함.
    private final JwtTokenProvider jwtTokenProvider;

    public LoginTestController(UserTestRepository userTestRepository, JwtRepository jwtRepository, JwtTokenProvider jwtTokenProvider){
        this.userTestRepository = userTestRepository;
        this.jwtRepository = jwtRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/user")
    public ResponseEntity<TokenPairDTO> user(OAuth2AuthenticationToken token){

        OAuth2User oauth2User = token.getPrincipal();
        Map<String, Object> tokenAttributes = oauth2User.getAttributes();

        //아래 두줄 코드 너무 위험함. 타입체크나 NULL체크같은거 넣는 습관 들이기.
        Long oauthName = Long.valueOf(tokenAttributes.get("id").toString());
        String nickname = ((Map<String, Object>)tokenAttributes.get("properties")).get("nickname").toString();
        boolean isFirstLogin;

        //accessToken, refreshToken 생성 메서드
        //토큰 유효성 검증 메서드
        //토큰에서 kakaoId 추출 - 카카오 측에서 전달해 준 토큰의 카카오 식별자 추출

        //서비스로 빼보기. 컨트롤러는 판단을 하지 않는다 - 정민 님
        if(userTestRepository.existsByOauthName(oauthName) == true){
            System.out.println("로그인 한 적이 있는 카카오 사용자입니다.");
            isFirstLogin = false;
        } else {
            System.out.println("처음 접속한 사용자입니다. DB에 정보를 저장합니다.");
            UserTest usertest = new UserTest(oauthName, nickname);
            isFirstLogin = true;
            userTestRepository.save(usertest);
        }

        TokenPairDTO tokenPairDTO = jwtTokenProvider.generateTokens(oauthName, isFirstLogin);
        tokenPairDTO.printTokens();

        String refreshForDB = tokenPairDTO.getRefreshToken();
        Claims refreshClaims = jwtTokenProvider.parseClaims(refreshForDB);

        RefreshToken refreshTokenEntity = new RefreshToken(
                oauthName,
                refreshForDB,
                refreshClaims.getIssuedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                refreshClaims.getExpiration().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
        );
        jwtRepository.save(refreshTokenEntity);

        //서비스로 빼보기.
        if(jwtTokenProvider.validateToken(tokenPairDTO.getAccessToken()) == true){
            System.out.println("Access토큰 유효성 검사(대충) : true");
        } else {
            System.out.println("Access토큰 유효성 검사(대충) : false");
        }

        //서비스로 빼보기
        if(jwtTokenProvider.validateToken(tokenPairDTO.getRefreshToken()) == true){
            System.out.println("Refresh토큰 유효성 검사(대충) : true");
        } else{
            System.out.println("Refresh토큰 유효성 검사(대충) : false");
        }

        //System.out.println("토큰으로부터 추출한 카카오 id : " + oauthName);
        //System.out.println("토큰으로부터 추출한 카카오 닉네임 : " + nickname);
        System.out.println("Attributes : " + oauth2User.getAttributes());

        return ResponseEntity.ok(tokenPairDTO);
    }

//    public OAuth2User user(Principal principal){
//        System.out.println("Attributes : " + principal.getAtt);
//        return principal.getPri;
//    }

    //리다이렉트 URL. 카카오 로그인 - 인가 코드 발급 + 토큰 받기까지 완료되면 이 URL로 이동합니다.
//    @GetMapping("/login/oauth2/code/kakao")
//    public String result(){
//        System.out.println("액세스 토큰 발급 완료");
//        return "액세스 토큰 발급 완료";
//    }
//
//    @GetMapping("/")
//    public String homeresult(){
//        System.out.println("베이스 URL로 이동했음");
//        return "베이스 URL로 이동했음";
//    }
//
//    @GetMapping("/token")
//    public String nowTokenPrint(OAuth2AuthenticationToken token){
//        if(token != null){
//        System.out.println("현재 토큰 정보 : " + token);
//        return "현재 토큰 정보 확인 중";
//        } else return "현재 발행된 토큰이 없습니다. 로그인을 먼저 진행해 주세요.";
//    }

    @GetMapping("/tokenvalidate")
    public ResponseEntity<String> validateTokenFromHeader(@RequestHeader("Authorization") String authHeader){

        // 스프링 인터셉터? WAS의 필터 등으로 매 요청마다 체크해줘야 함. 그래야 만료를 확인하고 재발급을 해줄 테니까.
        // 더불어 인가 여부도 확인해야 함. 이 코드는 로그인 상태인 사람들에게만 적용되고, 비로그인인 사람에게는 적용되지 않아야 하니까.
        //

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