package com.project.hiptour.common.web.auth;

import com.project.hiptour.common.security.OauthProviderService;
import com.project.hiptour.common.security.UserIdentity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LoginControllerTest {

    @MockBean
    OauthProviderService mockService;

    @Autowired
    MockMvc mvc;

    private String redirection = "/login/oauth2/code/kakao";

    @BeforeEach
    void init(){
        when(this.mockService.getRedirectUrl()).thenReturn(redirection);
        when(this.mockService.getUserIdentity(anyString())).thenReturn(
                new UserIdentity() {
                    @Override
                    public String getUserIdentifier() {
                        return "user";
                    }

                    @Override
                    public String getNickName() {
                        return "nick";
                    }

                    @Override
                    public long getUserId() {
                        return 1;
                    }
                }
        );
    }

    @Test
    @DisplayName("/login/kakao path를 호출하면, redirection된다")
    void t() throws Exception {

        mvc.perform(get("/login/kakao"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(redirection));

    }

    @Test
    @DisplayName("redirection 후, 토큰이 발급된다. 이때, 최초 가입자의 경우, 표시된다")
    void t1() throws Exception {

        mvc.perform(get("/login/kakao"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(redirection));

        mvc.perform(get(redirection).queryParam("code","code"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andExpect(jsonPath("$.signUpNeeded").value(true));

    }

    @Test
    @DisplayName("redirection 후, 토큰이 발급된다. 이때, 최초 가입자가 아닌 경우, 최초 가입자가 아님을 표시한다.")
    void t2() throws Exception {

        mvc.perform(get("/login/kakao"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(redirection));

        mvc.perform(get(redirection).queryParam("code","code"));

        mvc.perform(get(redirection).queryParam("code","code"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andExpect(jsonPath("$.signUpNeeded").value(false));


    }


}