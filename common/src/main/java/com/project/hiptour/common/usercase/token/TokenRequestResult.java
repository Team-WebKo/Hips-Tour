package com.project.hiptour.common.usercase.token;

import com.project.hiptour.common.usercase.services.token.Token;
import com.project.hiptour.common.usercase.services.token.TokenTemplate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor
@Builder
@Data
@ToString
public class TokenRequestResult
{
    private final boolean isSuccess;
    private final String message;
    private final String accessToken;
}
