package com.project.hiptour.common.usercase.services.login;


import com.project.hiptour.common.entity.users.UserInfo;
import com.project.hiptour.common.security.UserIdentity;

import java.util.Optional;

public interface UserService {
    Optional<UserInfo> findUserInfoByIdentifier(String userIdentifier);

    UserInfo insertNewUserAndGet(UserIdentity userIdentity);
}
