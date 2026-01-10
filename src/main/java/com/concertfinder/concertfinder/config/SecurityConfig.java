package com.concertfinder.concertfinder.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity security) throws Exception {



        security
                .formLogin(login -> login // 로그인 관련 설정
                        .loginPage("/user/login") // 유저컨트롤러와 연결되는 html 매핑주소
                        .loginProcessingUrl("/user/login") // 컨트롤러의 요청을 처리 할 restController 주소
                        .usernameParameter("userId") // 유저가 입력한 아이디의 파라미터 이름
                        .passwordParameter("password") // 유저가 입력한 비밀번호의 파라미터 이름
                        // .defaultSuccessUrl("/concert/list") // 로그인 성공시 리다이렉트 시킬 주소
                        .successHandler((request, response, authentication) -> {
                            response.setStatus(HttpStatus.OK.value());
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("{\"message\":\"로그인 성공\"}");
                        })
                        .failureHandler((request, response, exception) -> {
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("{\"message\":\"아이디 또는 비밀번호가 틀렸습니다.\"}");
                        })
                        .permitAll())
                // url 요청 권한 설정
                .authorizeHttpRequests(request ->
                request.anyRequest().permitAll());
        return security.build();
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }
}
