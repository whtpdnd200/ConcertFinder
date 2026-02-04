package com.concertfinder.concertfinder.jwt;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;

public class CookieUtil {

    // JWT 토큰 쿠키 저장 메서드
    public static void addSecureCookie(HttpServletResponse response, String name, String value, int maxAge) {

        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path("/")
                .httpOnly(true)
                .secure(false)
                .maxAge(maxAge)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

//    // CSRF 토큰 쿠키 저장 메서드
//    public static void addCsrfCookie(HttpServletResponse response, String name, String value, int maxAge) {
//
//        ResponseCookie cookie = ResponseCookie.from(name, value)
//                .path("/")
//                .httpOnly(false)
//                .secure(false)
//                .maxAge(maxAge)
//                .build();
//
//        response.addHeader("Set-Cookie", cookie.toString());
//    }

    // 쿠키에서 토큰 반환 메서드
    public static String getCookieToken(HttpServletRequest request, String name) {

        Cookie[] cookies = request.getCookies();

        if(cookies != null) {

            for(Cookie cookie : cookies) {

                if(name.equals(cookie.getName())) {

                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    // 쿠키 삭제 메서드
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {

            for (Cookie cookie : cookies) {

                if (name.equals(cookie.getName())) {
                    ResponseCookie deleteCookie = ResponseCookie.from(name, "")
                            .path("/")
                            .maxAge(0)
                            .build();
                    response.addHeader("Set-Cookie", deleteCookie.toString());
                }
            }
        }
    }
}
