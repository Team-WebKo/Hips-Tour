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
    private final LocalDateTime createdTime;
    private final LocalDateTime expireTime;

    public String getPayLoad(){
        return this.token.substring(token.indexOf(".")+1, token.lastIndexOf("."));
    }

}
