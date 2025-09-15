package com.project.hiptour.common.usercase.logout;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class LogoutResponse {
    private final boolean isSuccess;
    private final String message;
}
