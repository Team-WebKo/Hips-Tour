package com.project.hiptour.common.security.kakao;

import lombok.Data;

import java.util.Map;

@Data
public class KakaoUserResponse {
    private Long id;
    private Map<String, Object> kakao_account;
}
