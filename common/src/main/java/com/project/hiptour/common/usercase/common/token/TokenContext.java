package com.project.hiptour.common.usercase.common.token;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.Getter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Component
@Getter
@ToString
public class TokenContext {
    private final long ACCESSKEY_EXPIRE = 60 * 30;
    private final long REFRESH_EXPIRE = 7 * 24 * 60 * 60;
    private Algorithm algorithm;

    public TokenContext(KeyProvider keyProvider) {
        this.algorithm = Algorithm.HMAC256(keyProvider.getKey());
    }
}
