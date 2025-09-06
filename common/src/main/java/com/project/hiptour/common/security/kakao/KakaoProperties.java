package com.project.hiptour.common.security.kakao;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "oauth2.kakao")
public class KakaoProperties {

    private String clientId;
    private String redirectUri;
    private String baseurl;
    private String resourceUrl;
    private String authorizationGrantType;
    private String userInfoUri;
    private String tokenUri;
}
