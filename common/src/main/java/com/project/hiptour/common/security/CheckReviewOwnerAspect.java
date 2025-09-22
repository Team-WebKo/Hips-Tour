package com.project.hiptour.common.security;

import com.project.hiptour.common.entity.review.Review;
import com.project.hiptour.common.entity.review.repos.ReviewRepository;
import com.project.hiptour.common.exception.InvalidAccessException;
import com.project.hiptour.common.exception.review.ReviewAccessDeniedException;
import com.project.hiptour.common.exception.review.ReviewNotFoundException;
import com.project.hiptour.common.usercase.common.token.TokenTemplate;
import com.project.hiptour.common.usercase.services.token.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;
import java.util.Objects;

@Aspect
@Component
@RequiredArgsConstructor
public class CheckReviewOwnerAspect {
    private final TokenService tokenService;
    private final ReviewRepository reviewRepository;

    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    @Before("@annotation(com.project.hiptour.common.security.CheckReviewOwner)")
    public void checkOwner(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String header = request.getHeader(HEADER_AUTHORIZATION);
        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            throw new ReviewAccessDeniedException("인증 토큰이 필요합니다.");
        }

        String accessToken = header.substring(TOKEN_PREFIX.length());
        TokenTemplate tokenTemplate = tokenService.decodeToken(accessToken);
        Long currentUserId = tokenTemplate.getUserId();

        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Long reviewId = Long.parseLong(pathVariables.get("reviewId"));

        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewNotFoundException("리뷰를 찾을 수 없습니다: " + reviewId));
        Long authorId = review.getUserInfo().getUserId();

        if (!Objects.equals(currentUserId, authorId)) {
            throw new ReviewAccessDeniedException("리뷰에 대한 권한이 없습니다.");
        }
    }
}
