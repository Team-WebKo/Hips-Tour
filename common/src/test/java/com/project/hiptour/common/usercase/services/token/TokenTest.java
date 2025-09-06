package com.project.hiptour.common.usercase.services.token;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TokenTest {

    @Test
    @DisplayName("토큰 객체는, 토큰을 의미단위로 분해한다.")
    void tokenTest(){

        String sample = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkNoYXRHUFQgVXNlciIsImFkbWluIjp0cnVlLCJleHAiOjE3NTcxNjgzMzF9.aHEdp-67afk99urqnoj9fuL6VU5h1hL3gAhBBv_ya_o";
        Token token = new Token(sample, LocalDateTime.now(), LocalDateTime.now());

        assertEquals("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9", token.getHeader());
        assertEquals("eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkNoYXRHUFQgVXNlciIsImFkbWluIjp0cnVlLCJleHAiOjE3NTcxNjgzMzF9", token.getPayLoad());
        assertEquals("aHEdp-67afk99urqnoj9fuL6VU5h1hL3gAhBBv_ya_o", token.getSignature());


    }

}