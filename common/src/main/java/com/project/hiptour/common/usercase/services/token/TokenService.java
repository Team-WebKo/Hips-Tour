package com.project.hiptour.common.usercase.services.token;

import com.project.hiptour.common.entity.users.TokenInfo;
import com.project.hiptour.common.entity.users.UserInfo;

public interface TokenService {
    TokenPair createToken(UserInfo userInfo);
    void updateToken(Long userId, Token refreshToken);
    Token decodeToken(String token);
    TokenInfo findByUserId(long userId);
}
