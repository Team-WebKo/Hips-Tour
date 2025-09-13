package com.project.hiptour.common.web.auth;

import com.project.hiptour.common.usercase.UserLogoutUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/logout")
@RequiredArgsConstructor
@Slf4j
//TODO: 로그아웃 컨트롤러
public class LogoutController {

    private final UserLogoutUseCase userLogoutUseCase;

    @PostMapping
    public ResponseEntity<Map<String, String>> logout(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());

        userLogoutUseCase.logout(userId);

        return ResponseEntity.ok(Map.of("message", "로그아웃에 성공하였습니다."));
    }
}
