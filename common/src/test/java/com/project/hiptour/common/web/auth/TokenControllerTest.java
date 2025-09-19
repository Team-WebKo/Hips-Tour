package com.project.hiptour.common.web.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.hiptour.common.security.OauthProviderService;
import com.project.hiptour.common.usercase.common.token.TokenService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional  // 각 테스트 후 롤백
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TokenControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    TokenService tokenService;

    @MockBean
    OauthProviderService mockService;
    ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("token 정보를 받으면, 이를 검증하고, 잘못된 토큰인 경우 실패를 반환한다")
    void t() throws Exception {

        Map<String, String> refreshToken = Map.of("refreshToken", "invalidToken");
        String json = this.mapper.writeValueAsString(refreshToken);

        this.mvc.perform(post("/token")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.accessToken").value(""));

    }
}