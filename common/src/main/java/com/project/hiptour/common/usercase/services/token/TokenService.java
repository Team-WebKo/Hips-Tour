package com.project.hiptour.common.usercase.services.token;

import com.project.hiptour.common.entity.users.TokenInfo;
import com.project.hiptour.common.entity.users.UserInfo;
import com.project.hiptour.common.entity.users.UserRole;

import java.util.List;

public interface TokenService {
    TokenPair createToken(UserInfo userInfo, List<UserRole> userRoles);
    void updateToken(Long userId, Token refreshToken);
    /**
     * @return 만약 토큰 정보가 잘못되었다면, null을 반환할 수 있으므로 null체크가 필요
     * **/
    TokenTemplate decodeToken(String token);
    TokenInfo findByUserId(long userId);
}
