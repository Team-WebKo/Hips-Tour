package com.project.hiptour.common.oauth.dto;

public class TokenValidateDTO {

    private boolean valid;
    private String tokenSubject;
    private Long kakaoId;
    private long issuedAt;
    private long expiresAt;
    private long expiresInSeconds;

    public TokenValidateDTO(boolean valid,
                            String tokenSubject,
                            Long kakaoId,
                            long issuedAt,
                            long expiresAt,
                            long expiresInSeconds) {
        this.valid = valid;
        this.tokenSubject = tokenSubject;
        this.kakaoId = kakaoId;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.expiresInSeconds = expiresInSeconds;
    }

    //
    public boolean isValid() { return valid; }
    public String getTokenSubject() { return tokenSubject; }
    public Long getKakaoId() { return kakaoId; }
    public long getIssuedAt() { return issuedAt; }
    public long getExpiresAt() { return expiresAt; }
    public long getExpiresInSeconds() { return expiresInSeconds; }

}
