package com.project.hiptour.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix= "jwt")
public class JwtProperties {
    private String secret;
    private long accessValidityMs;
    private long refreshValidityMs;

    public String getSecret() {return secret;}
    public void setSecret(String secret) {this.secret = secret;}
    public long getAccessValidityMs() {return accessValidityMs;}
    public void setAccessValidityMs(long accessValidityMs) {this.accessValidityMs = accessValidityMs;}
    public long getRefreshValidityMs() {return refreshValidityMs;}
    public void setRefreshValidityMs(long refreshValidityMs) {this.refreshValidityMs = refreshValidityMs;}
}
