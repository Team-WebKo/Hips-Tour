package com.project.hiptour.common.usercase.common.token;

import com.auth0.jwt.JWT;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.boot.jackson.JsonObjectDeserializer;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

@AllArgsConstructor
@Getter
@ToString
public class Token {
    private final String token;
    private final String header;
    private final String payload;
    private final String signature;

    public Token(String token) {
        this.token = token;
        this.header = token.substring(0, token.indexOf("."));
        this.payload = token.substring(token.indexOf(".")+1, token.lastIndexOf("."));
        this.signature = token.substring(token.lastIndexOf(".")+1);
    }

    public LocalDateTime getExpireDate(){
        Date expiresAt = JWT.decode(this.token).getExpiresAt();
        Instant instant = expiresAt.toInstant();
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
