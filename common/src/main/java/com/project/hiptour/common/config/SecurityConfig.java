package com.project.hiptour.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   HandlerMappingIntrospector introspector) throws Exception {

        var pp = PathPatternRequestMatcher.withDefaults();
        RequestMatcher apiOnly = pp.matcher("/auth-test/**");
        AuthenticationEntryPoint unauthorized401 = new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);

        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/auth-test/**").permitAll()
                        .requestMatchers("/error", "/error/**").permitAll()
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
                .oauth2Login(oauth2 -> oauth2.defaultSuccessUrl("/user", true))
                .exceptionHandling(e -> e.defaultAuthenticationEntryPointFor(unauthorized401, apiOnly))
                .requestCache(c ->c.disable());

        return http.build();
    }

}
