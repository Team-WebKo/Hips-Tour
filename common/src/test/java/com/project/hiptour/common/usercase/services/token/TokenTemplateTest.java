package com.project.hiptour.common.usercase.services.token;

import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.Test;

import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TokenTemplateTest {

    @Test
    void t(){

        TokenContext context = new TokenContext("key"::getBytes);

        TokenTemplate template = new TokenTemplate(1, List.of(1L));
        Token refreshToken = template.toAccessToken(context);

        System.out.println(refreshToken);

        String token = refreshToken.getPayLoad();
        byte[] decode = Base64.getUrlDecoder().decode(token);
        System.out.println(new String(decode));
    }

}