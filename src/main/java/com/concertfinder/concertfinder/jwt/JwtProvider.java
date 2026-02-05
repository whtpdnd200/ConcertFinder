package com.concertfinder.concertfinder.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;


@Slf4j
@Component
public class JwtProvider {

    private final Key SECRET_KEY;

    private final long ACCESS_TOKEN_EXPIRATION;

    private final long REFRESH_TOKEN_EXPIRATION;

    // 생성자에서 설정값 주입 및 Key 초기화
    public JwtProvider(
            @Value("${spring.jwt.secret}") String secretKey
            , @Value("${spring.jwt.access-token-expiration}") long accessTokenExpiration
            , @Value("${spring.jwt.refresh-token-expiration}") long refreshTokenExpiration) {

        // 시크릿 키를 Byte 배열로 변환 후 HMAC SHA 알고리즘에 맞는 Key 객체 생성
        // 문자열을 암호화된 객체 규격으로 만들기 위함
        byte[] keyBytes = Decoders.BASE64.decode(Base64.getEncoder().encodeToString(secretKey.getBytes()));
        this.SECRET_KEY = Keys.hmacShaKeyFor(keyBytes);
        this.ACCESS_TOKEN_EXPIRATION = accessTokenExpiration;
        this.REFRESH_TOKEN_EXPIRATION = refreshTokenExpiration;
    }

    // 토큰 생성 메서드
    private String createToken(String userId, String role, long expiration) {

        Claims claims = Jwts.claims().setSubject(userId);
        if (role != null) {
            claims.put("role", role);
        } else {

            // role가 null인 경우는 리프레시 토큰
            // 리프레시 토큰에 자동로그인 여부 저장
            claims.put("isAutoLogin", true);
        }

        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 데이터 (email, role 등)
                .setIssuedAt(now)  // 발행 시간
                .setExpiration(new Date(now.getTime() + expiration)) // 만료 시간
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256) // 암호화 알고리즘과 키
                .compact();
    }

    // 엑세스 토큰 생성 메서드
    public String createAccessToken(String userId, String role) {

        return createToken(userId, role, ACCESS_TOKEN_EXPIRATION);
    }

    // 리프레시 토큰 생성 메서드
    public String createRefreshToken(String userId) {

        return createToken(userId, null, REFRESH_TOKEN_EXPIRATION);
    }

    // 토큰에서 유저 아이디 추출
    public String getUserId(String token) {

        try {
            return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build()
                    .parseClaimsJws(token).getBody().getSubject();
        } catch(ExpiredJwtException e) {

            return e.getClaims().getSubject();
        } catch(Exception e) {
            log.warn("파싱 할 수 없는 토큰 : {}", e.getMessage());
            return null;
        }
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    // 오염된 토큰인지 확인
    public boolean isExpired(String token) {

        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return false; // 만료 안 됐으면 false
        } catch (ExpiredJwtException e) {
            return true; // 만료되었을 때만 true
        } catch (Exception e) {
            // 서명 오류, 형식 오류 등은 만료가 아니라 오염이므로 false
            return false;
        }
    }
}
