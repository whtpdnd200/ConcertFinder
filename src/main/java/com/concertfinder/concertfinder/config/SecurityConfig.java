package com.concertfinder.concertfinder.config;

import com.concertfinder.concertfinder.filter.CsrfCookieFilter;
import com.concertfinder.concertfinder.filter.JwtAuthenticationFilter;
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
                 .build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

}
