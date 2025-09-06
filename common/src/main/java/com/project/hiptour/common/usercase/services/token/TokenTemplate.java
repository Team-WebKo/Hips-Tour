package com.project.hiptour.common.usercase.services.token;

import com.auth0.jwt.JWT;
import com.project.hiptour.common.entity.users.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class TokenTemplate {
    private long userId;
    private List<Long> roleIds;

    public TokenTemplate(Long userId, List<UserRole> userRoles) {
        this.userId = userId;
        this.roleIds = userRoles == null ? List.of() : userRoles.stream()
                .map(UserRole::getUserRoleId)
                .toList();
    }

    public Token toAccessToken(TokenContext context){
        Date expiresAt = new Date(System.currentTimeMillis() + context.getACCESSKEY_EXPIRE());
        String token = JWT.create()
                .withSubject(String.valueOf(this.userId))
                .withClaim("role", this.roleIds)
                .withIssuedAt(new Date())
                .withExpiresAt(expiresAt)
                .sign(context.getAlgorithm());
        return getToken(token);
    }

    public Token toRefreshToken(TokenContext context){
        Date expiresAt = new Date(System.currentTimeMillis() + context.getREFRESH_EXPIRE());
        String refreshToken = JWT.create()
                .withSubject(String.valueOf(this.userId))
                .withClaim("role", this.roleIds)
                .withIssuedAt(new Date())
                .withExpiresAt(expiresAt)
                .sign(context.getAlgorithm());
        return getToken(refreshToken);
    }

    private Token getToken(String refreshToken) {
        return new Token(refreshToken, LocalDateTime.now(), LocalDateTime.now());
    }
}
