package com.project.hiptour.common.security.kakao;

import com.project.hiptour.common.entity.users.repos.TokenRepos;
import com.project.hiptour.common.security.OauthProviderService;
import com.project.hiptour.common.security.UserIdentity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
@Slf4j
@Profile("!test")
public class KakaoProviderServiceImpl implements OauthProviderService {

    private final KakaoProperties properties;
    private final RestTemplate template = new RestTemplate();

    @Override
    public UserIdentity getUserIdentity(String code) {

        String url = properties.getTokenUri();

        HttpHeaders headerForToken = new HttpHeaders();
        headerForToken.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", properties.getAuthorizationGrantType());
        params.add("client_id", properties.getClientId());
        params.add("redirect_uri", properties.getRedirectUri());
        params.add("code", code);

        log.debug("created oatuh header for token request {}", params);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headerForToken);

        KakaoTokenResponse kakaoTokenResponse = template.postForObject(url, request, KakaoTokenResponse.class);
        String accessToken = kakaoTokenResponse.getAccess_token();

        log.debug("got kakao token {} response from auth server", accessToken.hashCode());

        HttpHeaders headerForResource = new HttpHeaders();
        headerForResource.setBearerAuth(accessToken);

        log.debug("create header for resource to resource server");

        HttpEntity<Void> reqForResource = new HttpEntity<>(headerForResource);

        KakaoUserResponse userResponse = template.exchange(this.properties.getUserInfoUri(), HttpMethod.GET, reqForResource, KakaoUserResponse.class).getBody();

        Long kakaoId = userResponse.getId();

        log.debug("got kakao id");

        return new KakaoUserIdentity(kakaoId);
    }

    @Override
    public String getRedirectUrl() {
        return "https://kauth.kakao.com/oauth/authorize"
                + "?response_type=code"
                + "&client_id=" + properties.getClientId()
                + "&redirect_uri=" + properties.getRedirectUri(); // 서버 콜백 URL;
    }
}
