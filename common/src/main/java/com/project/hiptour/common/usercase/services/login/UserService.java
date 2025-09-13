package com.project.hiptour.common.usercase.services.login;


import com.project.hiptour.common.entity.users.UserInfo;
import com.project.hiptour.common.security.UserIdentity;

public interface UserService {
    UserInfo insertNewUserAndGet(UserIdentity userIdentity);
}
