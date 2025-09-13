package com.project.hiptour.common.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.project.hiptour.common.usercase.services.token.Token;
import com.project.hiptour.common.usercase.services.token.TokenService;
import com.project.hiptour.common.usercase.services.token.TokenTemplate;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String header = request.getHeader(HEADER_AUTHORIZATION);
//
//        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        try {
//            String accessToken = header.substring(TOKEN_PREFIX.length());
//
//            TokenTemplate tokenTemplate = tokenService.decodeToken(accessToken);
//            long userId = tokenTemplate.getUserId();
//
//            Authentication authentication = new UsernamePasswordAuthenticationToken(
//                    userId, null, Collections.emptyList()
//            );
//
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        } catch (JWTVerificationException | NumberFormatException e) {}

        filterChain.doFilter(request, response);
    }
}
