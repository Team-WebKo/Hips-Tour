package com.project.hiptour.common.usercase.services.token;

import com.project.hiptour.common.entity.users.UserInfo;
import org.springframework.stereotype.Service;

@Service
public class DefaultTokenProvider implements TokenProvider{
    @Override
    public String generateToken(UserInfo user) {
        return "";
    }
}
