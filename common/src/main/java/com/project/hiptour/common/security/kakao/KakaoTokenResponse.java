package com.project.hiptour.common.security.kakao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class KakaoTokenResponse {
    private String token_type;
    private String access_token;
    private String refresh_token;
    private Integer expires_in;
    private Integer refresh_token_expires_in;
    private String scope;
}
