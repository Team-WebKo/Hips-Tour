package com.project.hiptour.common.config;

import com.project.hiptour.common.security.OauthProviderService;
import com.project.hiptour.common.usercase.services.token.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SecurityConfigTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenService tokenService;

    @MockBean
    private OauthProviderService oauthProviderService;

    @Test
    @DisplayName("인증이 필요 없는 public 경로는 토큰 없이 접근 가능합니다.")
    void public_path_accessible_without_token() throws Exception {

        mockMvc.perform(get("/some/public/path"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}
