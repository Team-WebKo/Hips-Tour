package com.project.hiptour.common.usercase.login;

import com.project.hiptour.common.usercase.common.token.TokenPair;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
public class LoginResult {
    private final boolean isNewSingUp;
    private final TokenPair pair;
    private final LocalDateTime createTime = LocalDateTime.now();
}
