package com.project.hiptour.common.usercase.services.token;

import com.project.hiptour.common.security.OauthProviderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles("test")
class TokenServiceTest {

    @MockBean
    OauthProviderService mockService;
    @Autowired
    TokenService tokenService;

    @Test
    void t(){


    }

}