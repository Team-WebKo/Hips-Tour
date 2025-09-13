package com.project.hiptour.common.web.auth.token;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TokenRequest {
    private String refreshToken;
}
