package com.project.hiptour.common.oauth.filter;

import com.project.hiptour.common.oauth.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 20)
public class AccessTokenRefreshFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public AccessTokenRefreshFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

//현재 필터는 /auth-test**/ 까지만 대상으로 하고, 재발급 엔드포인트들(/auth-test/refresh, /auth-test/validate...)는 제외된 상태.

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        if (path == null) return true;
        if (!path.startsWith("/auth-test/")) return true;
        // 재발급/검증 엔드포인트는 필터 패스
        if (path.startsWith("/auth-test/refresh")) return true;
        if (path.startsWith("/auth-test/validate-")) return true;
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws ServletException, IOException {

        String auth = req.getHeader("Authorization");
        String bearer = (auth != null && auth.startsWith("Bearer ")) ? auth.substring(7) : null;

        // 액세스 토큰이 유효하면 그대로 통과
        if (bearer != null && jwtTokenProvider.validateToken(bearer)) {
            chain.doFilter(req, res);
            return;
        }

        // 여기부터는 액세스가 없거나 만료/무효인 경우 -> 리프레시로 재발급 시도
        String refresh = extractRefreshToken(req);

        if (refresh != null && jwtTokenProvider.validateToken(refresh)) {
            Claims claims;
            try {
                claims = jwtTokenProvider.parseClaims(refresh);
            } catch (Exception e) {
                // 파싱 실패 시 그대로 진행(컨트롤러에서 401 처리)
                chain.doFilter(req, res);
                return;
            }

            if (!"refresh".equals(claims.getSubject())) {
                // 리프레시가 아닌 토큰이면 그대로 진행
                chain.doFilter(req, res);
                return;
            }

            Long kakaoId = claims.get("kakaoId", Long.class);
            if (kakaoId == null) {
                chain.doFilter(req, res);
                return;
            }

            // 새 액세스 발급
            String newAccess = jwtTokenProvider.generateAccessToken(kakaoId);

            // 로그 남기기
            System.out.println("[AUTH] Access token was expired/invalid. Reissued by refresh for kakaoId=" + kakaoId);

            // 요청 Authorization 헤더를 새 토큰으로 교체해서 컨트롤러까지 전달
            HttpServletRequest wrapped = new AuthorizationHeaderRequestWrapper(req, "Bearer " + newAccess);

            // 응답 헤더로도 내려줘서 클라이언트가 저장 교체 가능
            res.setHeader("X-New-Access-Token", newAccess);

            chain.doFilter(wrapped, res);
            return;
        }

        // 리프레시가 없거나 무효면 그대로 진행(컨트롤러가 401 반환)
        chain.doFilter(req, res);
    }

    /** 리프레시 토큰을 헤더 또는 쿠키에서 꺼낸다. 우선순위: 헤더 -> 쿠키 */
    private String extractRefreshToken(HttpServletRequest req) {
        // 1) 커스텀 헤더 시도
        String fromHeader = req.getHeader("X-Refresh-Token");
        if (fromHeader != null && !fromHeader.isBlank()) {
            return fromHeader.trim();
        }
        // 2) 쿠키 시도 (refreshToken 라는 이름 가정)
        if (req.getCookies() != null) {
            for (Cookie c : req.getCookies()) {
                if ("refreshToken".equals(c.getName()) && c.getValue() != null && !c.getValue().isBlank()) {
                    return c.getValue().trim();
                }
            }
        }
        return null;
    }

    /**
     * Authorization 헤더를 덮어쓸 수 있도록 Request 래퍼
     */
    private static class AuthorizationHeaderRequestWrapper extends HttpServletRequestWrapper {
        private final String newAuthorization;

        public AuthorizationHeaderRequestWrapper(HttpServletRequest request, String newAuthorization) {
            super(request);
            this.newAuthorization = newAuthorization;
        }

        @Override
        public String getHeader(String name) {
            if ("Authorization".equalsIgnoreCase(name)) {
                return newAuthorization;
            }
            return super.getHeader(name);
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            if ("Authorization".equalsIgnoreCase(name)) {
                return Collections.enumeration(Collections.singletonList(newAuthorization));
            }
            return super.getHeaders(name);
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            List<String> names = Collections.list(super.getHeaderNames());
            if (!names.stream().anyMatch(h -> "Authorization".equalsIgnoreCase(h))) {
                names.add("Authorization");
            }
            return Collections.enumeration(names);
        }
    }
}
