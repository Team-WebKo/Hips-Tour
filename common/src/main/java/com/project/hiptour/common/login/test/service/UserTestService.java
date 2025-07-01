package com.project.hiptour.common.login.test.service;

import com.project.hiptour.common.repository.UserTestRepository;

public class UserTestService {

    private final UserTestRepository userTestRepository;

    public UserTestService(UserTestRepository userTestRepository){
        this.userTestRepository = userTestRepository;
    }

    public boolean existsOauthNameByKakaoId(Long kakaoId){
        return userTestRepository.existsByOauthName(kakaoId);
    }

}
