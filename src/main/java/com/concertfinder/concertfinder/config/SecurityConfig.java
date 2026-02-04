package com.concertfinder.concertfinder.config;

import com.concertfinder.concertfinder.filter.CsrfCookieFilter;
import com.concertfinder.concertfinder.jwt.CookieUtil;
import com.concertfinder.concertfinder.jwt.JwtAuthenticationFilter;
import com.concertfinder.concertfinder.jwt.JwtProvider;
import com.concertfinder.concertfinder.user.service.PrincipalDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final PrincipalDetailsService principalDetailsService;
    private final RedisTemplate<String, Object> redisTemplate;


    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity security) throws Exception {

         return security
                 .csrf(csrf -> csrf
                         .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                         .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
                 .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                 .sessionManagement(session -> session.disable())
                 .formLogin(form -> form.disable())
                 .httpBasic(basic -> basic.disable())
                 .addFilterBefore(new JwtAuthenticationFilter(jwtProvider, principalDetailsService, redisTemplate)
                                 , org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
                 .exceptionHandling(exception -> exception
                         .authenticationEntryPoint(((request, response, authException) -> {
                             response.sendRedirect("/user/login");
                         })))
                 .authorizeHttpRequests(request ->

                         request.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/swagger-resources/**", "/favicon.ico", "/error").permitAll()
                                .requestMatchers("/css/**", "/js/**", "/favicon.ico").permitAll() // 기본 css js도 권한에 상관 없이 실행 되게
                                .requestMatchers(HttpMethod.POST, "/user").permitAll() // 회원가입 API 메서드는 누구나 실행 되게
                                .requestMatchers("/user/login", "/user/join", "/user/id-check").permitAll() // 로그인 없이 이동 가능한 페이지 및 API
                                .anyRequest().authenticated()) // 그외의 모든 기능은 로그인 해야 이용 가능
//                    .formLogin(login -> login // 로그인 관련 설정
//                            .loginPage("/user/login") // 유저컨트롤러와 연결되는 html 매핑주소
//                            .loginProcessingUrl("/user/login") // 스프링 시큐리티로 매핑 할 주소
//                            .usernameParameter("userId") // 유저가 입력한 아이디의 파라미터 이름
//                            .passwordParameter("password") // 유저가 입력한 비밀번호의 파라미터 이름
//                            .defaultSuccessUrl("/concert/list") // 로그인 성공시 리다이렉트 시킬 주소
//                            // ajax success 처럼 로그인 성공시 실행 할 내용
//                            .successHandler((request, response, authentication) -> {
//                                response.setStatus(HttpStatus.OK.value()); // http 상태코드 200 설정
//                                response.setContentType("application/json;charset=UTF-8"); // 응답 타입 JSON, 인코딩 utf8
//                                response.getWriter().write("{\"message\":\"로그인 성공\"}"); // 보낼 메세지
//                            })
//                            // ajax error 처럼 로그인 실패시 실행 할 내용
//                            .failureHandler((request, response, exception) -> {
//                                String errorMessage = "아이디 혹은 비밀번호가 일치하지 않습니다!";
//                                response.setStatus(HttpStatus.UNAUTHORIZED.value());
//
//                                response.setContentType("application/json;charset=UTF-8");
//                                if (exception instanceof DisabledException) {
//                                    response.setStatus(HttpStatus.FORBIDDEN.value()); // 403으로 변경
//                                    errorMessage = "탈퇴 한 회원 입니다!";
//                                }
//                                response.getWriter().write("{\"message\":\"" + errorMessage + "\"}");
//                            })
//                            .permitAll())
//                    // 로그아웃 설정
//                    .logout(logout -> logout
//                            .logoutUrl("/user/logout") // 스프링 시큐리티로 매핑 될 주소
//                            .logoutSuccessUrl("/user/login")
//                            .permitAll())
                    .build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

}
