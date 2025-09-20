package com.project.hiptour.common.usercase.common.token;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TokenPair {
    private final Token accessToken;
    private final Token refreshToken;
}
