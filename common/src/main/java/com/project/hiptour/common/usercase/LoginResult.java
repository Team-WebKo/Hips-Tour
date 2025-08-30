package com.project.hiptour.common.usercase;

import com.project.hiptour.common.usercase.services.token.TokenPair;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
public class LoginResult {
    private final boolean isNewSingUp;
    private final TokenPair pair;
    private final LocalDateTime createTime = LocalDateTime.now();
}
