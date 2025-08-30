package com.project.hiptour.common.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@AllArgsConstructor
@Configuration
public class SecurityConfig {

    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> kakaoSecurityService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   HandlerMappingIntrospector introspector) throws Exception {

        var pp = PathPatternRequestMatcher.withDefaults();
        RequestMatcher apiOnly = pp.matcher("/auth-test/**");
        AuthenticationEntryPoint unauthorized401 = new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);

        http
                .authorizeHttpRequests(authorize -> authorize
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
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .exceptionHandling(e -> e.defaultAuthenticationEntryPointFor(unauthorized401, apiOnly))
                .requestCache(RequestCacheConfigurer::disable);

        return http.build();
    }

}
