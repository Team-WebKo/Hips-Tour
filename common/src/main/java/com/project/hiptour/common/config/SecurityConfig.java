package com.project.hiptour.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/h2-console/**",
                                "/user",
                                "/user/**",
                                "/oauth2/**",
                                "/login/**"
                        ).permitAll()
                        .anyRequest().authenticated()// ✅ 인증 없이 허용
                )
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .oauth2Login(oauth2 -> oauth2.defaultSuccessUrl("/user", true)
                );

        return http.build();
    }

}
