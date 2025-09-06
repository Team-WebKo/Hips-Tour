package com.project.hiptour.common.usercase.services.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.hiptour.common.entity.users.TokenInfo;
import com.project.hiptour.common.entity.users.UserInfo;
import com.project.hiptour.common.entity.users.UserRole;
import com.project.hiptour.common.entity.users.repos.TokenRepos;
import com.project.hiptour.common.entity.users.repos.UserRoleRepo;
import com.project.hiptour.common.util.DateUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class DefaultTokenServiceImpl implements TokenService{

    private final long ACCESSKEY_EXPIRE = 60 * 30;
    private final long REFRESH_EXPIRE = 7 * 24 * 60 * 60;

    private Algorithm algorithm;
    private final TokenRepos tokenRepos;
    private final UserRoleRepo userRoleRepo;

    public DefaultTokenServiceImpl(KeyProvider keyProvider, TokenRepos tokenRepos, UserRoleRepo repos) {
        this.algorithm = Algorithm.HMAC256(keyProvider.getKey());
        this.tokenRepos = tokenRepos;
        this.userRoleRepo = repos;
    }

    @Override
    public TokenPair createToken(UserInfo userInfo) {

        List<UserRole> userRoles = this.userRoleRepo.findByUserInfo(userInfo).orElse(List.of());

        Date expiresAt = new Date(System.currentTimeMillis() + ACCESSKEY_EXPIRE);
        String accessToken = JWT.create()
                .withSubject(String.valueOf(userInfo.getUserId()))
                .withClaim("role", userRoles)
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
    public TokenTemplate decodeToken(String token) {
        try {
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .build()
                    .verify(token);

            String payload = decodedJWT.getPayload();
            byte[] decode = Base64.getUrlDecoder().decode(payload);
            String tokenInfo = new String(decode);

            ObjectMapper mapper = new ObjectMapper();
            try {
                Map claim = mapper.readValue(tokenInfo, Map.class);
                long sub = Long.valueOf((String) claim.get("sub"));
                return new TokenTemplate(sub,List.of());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }catch (JWTDecodeException e){
            log.warn("invalid token {}",e.getMessage());
            return null;
        }
    }

    @Override
    public TokenInfo findByUserId(long userId) {
        return this.tokenRepos.findByUserId(userId);
    }

}
