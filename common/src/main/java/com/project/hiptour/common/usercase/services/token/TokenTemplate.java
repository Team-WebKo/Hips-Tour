package com.project.hiptour.common.usercase.services.token;

import com.auth0.jwt.JWT;
import com.project.hiptour.common.entity.users.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class TokenTemplate {
    private long userId;
    private List<Long> roleIds;

    public TokenTemplate(Long userId, List<Long> userRoles) {
        this.userId = userId;
        this.roleIds = userRoles == null ? List.of() :userRoles;
    }

    public Token toAccessToken(TokenContext context){

        String token = JWT.create()
                .withSubject(String.valueOf(this.userId))
                .withClaim("role", this.roleIds)
                .withIssuedAt(getCurrentDate())
                .withExpiresAt(getExpiryDate(context.getACCESSKEY_EXPIRE()))
                .sign(context.getAlgorithm());
        return getToken(token);
    }

    public Token toRefreshToken(TokenContext context){
        String refreshToken = JWT.create()
                .withSubject(String.valueOf(this.userId))
                .withIssuedAt(getCurrentDate())
                .withExpiresAt(getExpiryDate(context.getREFRESH_EXPIRE()))
                .sign(context.getAlgorithm());
        return getToken(refreshToken);
    }


    private Date getCurrentDate() {
        // 현재로부터 1시간 뒤
        return Date.from(Instant.now());
    }


    private Date getExpiryDate(long time) {
        // 현재로부터 1시간 뒤
        return Date.from(Instant.now().plusSeconds(time));
    }

    private Token getToken(String refreshToken) {
        return new Token(refreshToken);
    }
}
