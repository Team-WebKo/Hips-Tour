package com.project.hiptour.common.usercase.services.token;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.security.Key;

/**
 * @apiNote 본 클래스는, 로컬 환경에서만 사용해야 함.
 * **/
@Component
@Profile({"test","local"})
public class InMemoryKeyProviderImpl implements KeyProvider{

    private static final String SECRET = "my-secret-key-12345"; // 실제 서비스에서는 환경변수/Vault

    @Override
    public byte[] getKey() {

        return SECRET.getBytes();
    }
}
