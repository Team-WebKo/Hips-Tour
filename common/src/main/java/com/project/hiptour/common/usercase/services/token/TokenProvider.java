package com.project.hiptour.common.usercase.services.token;

import com.project.hiptour.common.entity.users.UserInfo;
import com.project.hiptour.common.security.UserIdentity;

public interface TokenProvider {
    String generateToken(UserInfo user);
}
