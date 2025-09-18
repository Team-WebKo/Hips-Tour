package com.project.hiptour.common.usercase.services.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.hiptour.common.entity.users.TokenInfo;
import com.project.hiptour.common.entity.users.UserInfo;
import com.project.hiptour.common.entity.users.repos.TokenRepos;
import com.project.hiptour.common.entity.users.repos.UserRoleRepo;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class DefaultTokenServiceImpl implements TokenService {

    private final TokenContext tokenContext;
    private final TokenRepos tokenRepos;
    private final ObjectMapper mapper = new ObjectMapper();

    public DefaultTokenServiceImpl(TokenContext tokenContext, TokenRepos tokenRepos, UserRoleRepo repos) {
        this.tokenContext = tokenContext;
        this.tokenRepos = tokenRepos;
    }

    @Override
    public TokenPair createToken(UserInfo userInfo, List<Long> userRoles) {

        TokenTemplate template = new TokenTemplate(userInfo.getUserId(), userRoles);
        Token accessToken = template.toAccessToken(this.tokenContext);
        Token refreshToken = template.toRefreshToken(this.tokenContext);

        return new TokenPair(accessToken, refreshToken);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    @Override
    public void updateToken(Long userId, Token refreshToken) {

        Optional<TokenInfo> lastUserToken = this.tokenRepos.findFirstByUserIdOrderByCreatedAtDesc(userId);
        lastUserToken.ifPresent(TokenInfo::deactivate);

        TokenInfo tokenInfo = TokenInfo.builder()
                .refreshToken(refreshToken.getToken())
                .userId(userId)
                .build();

        this.tokenRepos.save(tokenInfo);

    }

    @Override
    public TokenTemplate decodeToken(String token) {
        try {
            DecodedJWT decodedJWT = JWT.require(this.tokenContext.getAlgorithm())
                    .build()
                    .verify(token);

            String payload = decodedJWT.getPayload();
            byte[] decode = Base64.getUrlDecoder().decode(payload);
            String tokenInfo = new String(decode);

            Map payloadAsMap = this.mapper.readValue(tokenInfo, Map.class);
            long userId = Long.parseLong((String) payloadAsMap.getOrDefault("sub", 0));
            List<Long> roles = (List<Long>) payloadAsMap.getOrDefault("role", List.of());
            return new TokenTemplate(userId, roles);

        } catch (JWTDecodeException e) {
            log.warn("invalid token!!! {}", e.getMessage());
            return null;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
