package com.concertfinder.concertfinder.interceptor;

import com.concertfinder.concertfinder.user.DTO.PrincipalDetails;
import groovy.util.logging.Slf4j;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

@lombok.extern.slf4j.Slf4j
@Slf4j
public class HttpHandshakeInterceptor extends HttpSessionHandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request
                                   , ServerHttpResponse response
                                   , WebSocketHandler webSocketHandler
                                   , Map<String, Object> attributes) throws Exception {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        PrincipalDetails userDetails = (PrincipalDetails)principal;

        Long userId =  userDetails.getLoginUserDTO().getId();

        if(userId != null) {
            attributes.put("userId", userId);
        }

        String[] path = request.getURI().getPath().split("/");

        String roomId = path[path.length - 1];

        if (roomId != null) {

            attributes.put("roomId", Long.parseLong(roomId));
        }

        return true;
    }
}
