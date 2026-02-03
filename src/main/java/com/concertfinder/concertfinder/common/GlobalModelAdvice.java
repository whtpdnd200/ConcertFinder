package com.concertfinder.concertfinder.common;

import com.concertfinder.concertfinder.jwt.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.UUID;

@ControllerAdvice
public class GlobalModelAdvice {

    @ModelAttribute
    public void addAttributes(HttpServletRequest request, HttpServletResponse response, Model model) {

        String csrfToken = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("XSRF-TOKEN".equals(cookie.getName())) {
                    csrfToken = cookie.getValue();
                    break;
                }
            }
        }

        if (csrfToken == null) {
            csrfToken = UUID.randomUUID().toString();
            CookieUtil.addCsrfCookie(response, "XSRF-TOKEN", csrfToken, 1800);
        }

        model.addAttribute("_csrfToken", csrfToken);
    }
}
