package com.project.hiptour.common.web.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/logout")
@AllArgsConstructor
@Slf4j
public class LogoutController {
    //TODO: 로그아웃 컨트롤러

    // 쿠키에서 RT 꺼내기
    // DB/Redis에서 무효화 처리
    // 쿠키 삭제 응답 던져주기
    @PostMapping
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> body = new HashMap<>();
        body.put("message", "Logout Successful");
        return ResponseEntity.ok(body);
    }
}
