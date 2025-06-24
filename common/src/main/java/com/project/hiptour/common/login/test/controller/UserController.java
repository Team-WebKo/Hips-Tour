package com.project.hiptour.common.login.test.controller;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserController {

    @GetMapping("/user")
    public OAuth2User user(Principal principal){
        return (OAuth2User) principal;
    }

}
