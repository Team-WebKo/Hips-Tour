package com.project.hiptour.common.web.auth.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class LoginReqResponse {
    private final String accessToken;
    private final String refreshToken;
    private final boolean isSignUpNeeded;

}

