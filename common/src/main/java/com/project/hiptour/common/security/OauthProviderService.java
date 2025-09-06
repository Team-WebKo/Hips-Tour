package com.project.hiptour.common.security;

public interface OauthProviderService {
    UserIdentity getUserIdentity(String code);
    String getRedirectUrl();
}
