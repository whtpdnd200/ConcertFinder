package com.concertfinder.concertfinder.filter;

import com.concertfinder.concertfinder.jwt.CookieUtil;
import com.concertfinder.concertfinder.jwt.JwtProvider;
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

            try {
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

                        } else {

                            log.warn("리프레시 토큰 불일치 User: {} ", userId);
                            forceLogout(response, userId);
                            return;
                        }
                    }
                } else {

                    log.warn("토큰 변조 토큰 제거 로직 실행");

                    forceLogout(response, jwtProvider.getUserId(refreshToken));
                    return;
                }
            } catch(Exception e) {

                log.error("토큰 형식 에러");
                forceLogout(response, jwtProvider.getUserId(refreshToken));
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

    private void forceLogout(HttpServletResponse response, String userId) throws IOException {

        if (userId != null) {
            try {
                redisTemplate.delete("refreshToken:" + userId);
                redisTemplate.delete("user:info:" + userId);
            } catch (Exception e) {
                log.error("Redis 삭제 실패: {}", e.getMessage());
            }
        }

        CookieUtil.addSecureCookie(response, "ACCESS_TOKEN", null, 0);
        CookieUtil.addSecureCookie(response, "REFRESH_TOKEN", null, 0);
        // CookieUtil.addCsrfCookie(response, "XSRF-TOKEN", null, 0);

        SecurityContextHolder.clearContext();
        response.sendRedirect("/user/login");
    }
}
