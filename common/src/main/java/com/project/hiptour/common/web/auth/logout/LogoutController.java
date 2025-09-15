package com.project.hiptour.common.web.auth.logout;

import com.project.hiptour.common.usercase.logout.LogoutResponse;
import com.project.hiptour.common.usercase.logout.UserLogoutUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/logout")
@RequiredArgsConstructor
@Slf4j
public class LogoutController {

    private final UserLogoutUseCase userLogoutUseCase;

    @PostMapping
    public ResponseEntity<Map<String, String>> logout(@RequestHeader("Authorization") String authorizationHeader) {
        LogoutResponse logoutResponse = userLogoutUseCase.logout(authorizationHeader);

        if(logoutResponse.isSuccess()){
            return ResponseEntity.ok(Map.of("message", "로그아웃에 성공했습니다."));
        }

        return ResponseEntity.badRequest().body(Map.of("message", logoutResponse.getMessage()));
    }
}
