//package com.project.hiptour.common.config;
//
//import com.project.hiptour.common.security.JwtAuthorizationFilter;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
////@Configuration
////@EnableWebSecurity
//@RequiredArgsConstructor
//public class SecurityConfig {
//    private final JwtAuthorizationFilter jwtAuthorizationFilter;
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity
//                .httpBasic(basic -> basic.disable())
//                .csrf(csrf -> csrf.disable())
//
//                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//
//                .authorizeHttpRequests(requests -> requests
//                        .anyRequest().permitAll())
//
//                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return httpSecurity.build();
//    }
//}
