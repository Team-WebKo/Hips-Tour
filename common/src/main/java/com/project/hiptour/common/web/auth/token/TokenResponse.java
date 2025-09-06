package com.project.hiptour.common.web.auth.token;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenResponse {
    private final String message;
    private final String accessToken;
}
