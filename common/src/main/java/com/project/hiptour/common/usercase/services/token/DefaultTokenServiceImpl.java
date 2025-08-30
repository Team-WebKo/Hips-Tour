package com.project.hiptour.common.usercase.services.token;

import com.project.hiptour.common.entity.users.UserInfo;

public class DefaultTokenServiceImpl implements TokenService{
    @Override
    public TokenPair createToken(UserInfo userInfo) {
        return null;
    }

    @Override
    public void updateToken(Token refreshToken) {

    }
}
