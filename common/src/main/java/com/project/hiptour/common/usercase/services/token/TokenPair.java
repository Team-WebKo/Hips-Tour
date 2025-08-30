package com.project.hiptour.common.usercase.services.token;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TokenPair {
    private final Token accessToken;
    private final Token refreshToken;
}
