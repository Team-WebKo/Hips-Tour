package com.project.hiptour.common.usercase.services.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.project.hiptour.common.entity.users.TokenInfo;
import com.project.hiptour.common.entity.users.UserInfo;
import com.project.hiptour.common.entity.users.repos.TokenRepos;
import com.project.hiptour.common.util.DateUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.Date;

@Service
public class DefaultTokenServiceImpl implements TokenService{

    private final long ACCESSKEY_EXPIRE = 60 * 30;
    private final long REFRESH_EXPIRE = 7 * 24 * 60 * 60;

    private Algorithm algorithm;
    private final TokenRepos tokenRepos;

    public DefaultTokenServiceImpl(KeyProvider keyProvider, TokenRepos tokenRepos) {
        this.algorithm = Algorithm.HMAC256(keyProvider.getKey());
        this.tokenRepos = tokenRepos;
    }

    @Override
    public TokenPair createToken(UserInfo userInfo) {
        Date expiresAt = new Date(System.currentTimeMillis() + ACCESSKEY_EXPIRE);
        String accessToken = JWT.create()
                .withSubject(String.valueOf(userInfo.getUserId()))
                .withClaim("role", "")
                .withIssuedAt(new Date())
                .withExpiresAt(expiresAt)
                .sign(algorithm);

        Token acctoken = new Token(accessToken, LocalDateTime.now(), LocalDateTime.now().minusMinutes(30));

        String refreshToken = JWT.create()
                .withSubject(String.valueOf(userInfo.getUserId()))
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_EXPIRE))
                .sign(algorithm);

        Token refToken = new Token(refreshToken, LocalDateTime.now(), LocalDateTime.now().plusDays(7));

        return new TokenPair(acctoken, refToken);
    }
    @Transactional(Transactional.TxType.SUPPORTS)
    @Override
    public void updateToken(Long userId, Token refreshToken) {
        TokenInfo tokenInfo = TokenInfo.builder()
                .refreshToken(refreshToken.getToken())
                .userId(userId)
                .build();

        this.tokenRepos.save(tokenInfo);

    }

    @Override
    public Token decodeToken(String token) {
        DecodedJWT decodedJWT = JWT.require(algorithm)
                .build()
                .verify(token);

        return new Token(
                decodedJWT.getToken(),
                LocalDateTime.now(),
                DateUtils.convert(decodedJWT.getExpiresAt()));
    }

    @Override
    public TokenInfo findByUserId(long userId) {
        return this.tokenRepos.findByUserId(userId);
    }

}
