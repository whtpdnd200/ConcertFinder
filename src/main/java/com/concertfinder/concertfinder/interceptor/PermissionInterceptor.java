//package com.concertfinder.concertfinder.interceptor;
//
//import com.concertfinder.concertfinder.user.DTO.LoginUserDTO;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import java.io.IOException;
//
//@Component
//public class PermissionInterceptor implements HandlerInterceptor {
//
//
//    @Override
//    public boolean preHandle(HttpServletRequest request
//                             , HttpServletResponse response
//                             , Object handler) throws IOException {
//
//        HttpSession session = request.getSession();
//
//        LoginUserDTO loginUserDTO = (LoginUserDTO)session.getAttribute("userInfo");
//
//        String uri = request.getRequestURI();
//        // 로그인이 안된 상태에서 메모와 관련된 페이지 접근을 막는다
//        if(loginUserDTO == null) {
//            // /post 로 시작하는 요청 url인 경우
//            if(uri.startsWith("/post") || uri.startsWith("/concert") || uri.equals("/user/mypage")) {
//
//                // 현재 접근하는 요청을 막고, 로그인 페이지로 리다이렉트
//                response.sendRedirect("/user/login");
//                return false;
//            }
//        } else {
//            // 로그인이 된 경우 로그인 회원가입 관련 페이지 접근 막기
//            if(uri.equals("/user/join") || uri.equals("/user/login")) {
//                response.sendRedirect("/concert/list");
//                return false;
//            }
//        }
//        return true;
//    }
//}
