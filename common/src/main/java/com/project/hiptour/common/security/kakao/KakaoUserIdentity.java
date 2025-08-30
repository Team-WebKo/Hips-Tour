package com.project.hiptour.common.security.kakao;

import com.project.hiptour.common.security.UserIdentity;
import lombok.AllArgsConstructor;

import java.util.Random;

@AllArgsConstructor
public class KakaoUserIdentity implements UserIdentity {

    private final long userId;

    @Override
    public String getUserIdentifier() {
        return "provider-kakao-"+this.getUserIdentifier();
    }

    @Override
    public String getNickName() {
        return "kakao-"+new Random().doubles();
    }
}
