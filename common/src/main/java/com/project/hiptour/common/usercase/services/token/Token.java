package com.project.hiptour.common.usercase.services.token;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@ToString
public class Token {
    private final String token;
    private final String header;
    private final String payload;
    private final String signature;
    private final LocalDateTime createdTime;
    private final LocalDateTime expireTime;

    public Token(String token, LocalDateTime createdTime, LocalDateTime expireTime) {
        this.token = token;
        this.createdTime = createdTime;
        this.expireTime = expireTime;
        this.header = token.substring(0, token.indexOf("."));
        this.payload = token.substring(token.indexOf(".")+1, token.lastIndexOf("."));
        this.signature = token.substring(token.lastIndexOf(".")+1);
    }

    public String getPayLoad(){
        return this.payload;
    }

}
