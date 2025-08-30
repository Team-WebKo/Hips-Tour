package com.project.hiptour.common.usercase.services.login;

import com.project.hiptour.common.entity.users.UserInfo;
import com.project.hiptour.common.entity.users.repos.UserRepos;
import com.project.hiptour.common.security.UserIdentity;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Transactional(value = Transactional.TxType.SUPPORTS)
@Service
@AllArgsConstructor
public class DefaultUserServiceImpl implements UserService {

    private final UserRepos userRepos;

    @Override
    public Optional<UserInfo> findUserInfoByIdentifier(String userIdentifier) {
        return this.userRepos.findByUserIdentifier(userIdentifier);
    }

    @Override
    public UserInfo insertNewUserAndGet(UserIdentity userIdentity) {

        UserInfo userInfo = UserInfo.builder()
                .userIdentifier(userIdentity.getUserIdentifier())
                .nickName(userIdentity.getNickName())
                .build();

        this.userRepos.save(userInfo);

        return userInfo;
    }
}
