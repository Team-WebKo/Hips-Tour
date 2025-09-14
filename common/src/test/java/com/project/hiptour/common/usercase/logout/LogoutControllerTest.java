package com.project.hiptour.common.usercase.logout;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.hiptour.common.usercase.UserLogoutUseCase;
import com.project.hiptour.common.web.auth.logout.LogoutRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LogoutControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserLogoutUseCase userLogoutUseCase;

    @Test
    @DisplayName("POST /logout 요청 시, userId를 받아 로그아웃을 처리하고 성공 메시지를 반환한다.")
    void process_logout_and_return_success_message() throws Exception {
        Long userId = 1L;
        LogoutRequestDto requestDto = new LogoutRequestDto();
        requestDto.setUserId(userId);

        mockMvc.perform(post("/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("로그아웃에 성공했습니다."));

        verify(userLogoutUseCase, times(1)).logout(userId);
    }
}
