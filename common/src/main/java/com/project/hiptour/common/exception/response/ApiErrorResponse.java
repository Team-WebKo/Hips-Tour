package com.project.hiptour.common.exception.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Builder;
import java.time.LocalDateTime;

@Getter
public class ApiErrorResponse {
    private final int status;
    private final String message;

    public ApiErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
