package com.concertfinder.concertfinder.interceptor;

import groovy.util.logging.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
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

        String[] path = request.getURI().getPath().split("/");

        String roomId = path[path.length - 1];

        if (roomId != null) {

            attributes.put("roomId", Long.parseLong(roomId));
        }

        return true;
    }
}
