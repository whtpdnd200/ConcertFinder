package com.concertfinder.concertfinder.jwt;

import com.concertfinder.concertfinder.user.DTO.PrincipalDetails;
import com.concertfinder.concertfinder.user.service.PrincipalDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final PrincipalDetailsService principalDetailsService;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String accessToken = CookieUtil.getCookieToken(request, "ACCESS_TOKEN");
        String refreshToken = CookieUtil.getCookieToken(request, "REFRESH_TOKEN");

        if(accessToken != null) {

            if(jwtProvider.validateToken(accessToken)) {

                setAuthentication(accessToken);

            } else if(jwtProvider.isExpired(accessToken)) {

                if(refreshToken != null && jwtProvider.validateToken(refreshToken)) {

                    String userId = jwtProvider.getUserId(refreshToken);

                    String cacheRefreshToken = (String)redisTemplate.opsForValue().get("refreshToken:" + userId);

                    if(refreshToken.equals(cacheRefreshToken)) {

                        log.info("Access Token 재발급");

                        String role = setAuthentication(refreshToken);

                        String newAccessToken = jwtProvider.createAccessToken(userId, role);
                        String newRefreshToken = jwtProvider.createRefreshToken(userId);

                        redisTemplate.opsForValue().set("refreshToken:" + userId, newRefreshToken, java.time.Duration.ofSeconds(2592000));
                        CookieUtil.addSecureCookie(response, "ACCESS_TOKEN", newAccessToken, 1800);
                        CookieUtil.addSecureCookie(response, "REFRESH_TOKEN", newRefreshToken, 2592000);

                    }
                }
            } else {

                log.warn("토큰 변조됨 토큰 제거 로직 실행");

                try {
                    String userId = jwtProvider.getUserId(accessToken);
                    redisTemplate.delete("refreshToken:" + userId);
                    redisTemplate.delete("user:info:" + userId);

                } catch (Exception e) {
                    log.error("변조된 토큰에서 userId 추출 실패: {}", e.getMessage());
                }

                CookieUtil.addSecureCookie(response, "ACCESS_TOKEN", null, 0);
                CookieUtil.addSecureCookie(response, "REFRESH_TOKEN", null, 0);
                CookieUtil.addCsrfCookie(response, "XSRF-TOKEN", null, 0);

                SecurityContextHolder.clearContext();

                response.sendRedirect("/user/login");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    // SecurityContext에 인증 정보 등록 메서드
    private String setAuthentication(String token) {

        String userId = jwtProvider.getUserId(token);

        PrincipalDetails userDetails = (PrincipalDetails)principalDetailsService.loadUserByUsername(userId);

        if(userDetails != null) {

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails
                    , null
                    , userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return userDetails.getRole();
        }

        return null;
    }
}
