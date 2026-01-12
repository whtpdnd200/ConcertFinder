package com.concertfinder.concertfinder.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity security) throws Exception {

        security
                // url 요청 권한 설정
                .authorizeHttpRequests(request ->
                        request.requestMatchers("/css/**", "/js/**", "/favicon.ico").permitAll() // 기본 css js도 권한에 상관 없이 실행 되게
                                .requestMatchers(HttpMethod.POST, "/user").permitAll() // 회원가입 API 메서드는 누구나 실행 되게
                                .requestMatchers("/user/login", "/user/join", "/user/id-check").permitAll() // 로그인 없이 이동 가능한 페이지 및 API
                                .anyRequest().authenticated()) // 그외의 모든 기능은 로그인 해야 이용 가능
                .formLogin(login -> login // 로그인 관련 설정
                        .loginPage("/user/login") // 유저컨트롤러와 연결되는 html 매핑주소
                        .loginProcessingUrl("/user/login") // 스프링 시큐리티로 매핑 할 주소
                        .usernameParameter("userId") // 유저가 입력한 아이디의 파라미터 이름
                        .passwordParameter("password") // 유저가 입력한 비밀번호의 파라미터 이름
                        .defaultSuccessUrl("/concert/list") // 로그인 성공시 리다이렉트 시킬 주소
                        // ajax success 처럼 로그인 성공시 실행 할 내용
                        .successHandler((request, response, authentication) -> {
                            response.setStatus(HttpStatus.OK.value()); // http 상태코드 200 설정
                            response.setContentType("application/json;charset=UTF-8"); // 응답 타입 JSON, 인코딩 utf8
                            response.getWriter().write("{\"message\":\"로그인 성공\"}"); // 보낼 메세지
                        })
                        // ajax error 처럼 로그인 실패시 실행 할 내용
                        .failureHandler((request, response, exception) -> {
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.setContentType("application/json;charset=UTF-8");
                            String errorMessage = "";

                            // 💡 예외 종류에 따른 메시지 설정
                            if (exception instanceof InternalAuthenticationServiceException) {

                                errorMessage = "존재하지 않는 아이디입니다.";
                            } else if (exception instanceof BadCredentialsException) {
                                // 비밀번호가 틀렸을 때
                                errorMessage = "비밀번호가 일치하지 않습니다.";
                            } else if (exception instanceof UsernameNotFoundException) {
                                errorMessage = "계정을 찾을 수 없습니다.";
                            }
                            response.getWriter().write("{\"message\":\"" + errorMessage + "\"}");
                        })
                        .permitAll())
                // 로그아웃 설정
                .logout(logout -> logout
                        .logoutUrl("/user/logout") // 스프링 시큐리티로 매핑 될 주소
                        .logoutSuccessUrl("/user/login")
                        .permitAll());
        return security.build();
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

}
