package com.concertfinder.concertfinder.config;

import com.concertfinder.concertfinder.common.SimpleWebSocketHandler;
import com.concertfinder.concertfinder.interceptor.HttpHandshakeInterceptor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final SimpleWebSocketHandler simpleWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry registry) {

        registry.addHandler(simpleWebSocketHandler, "/ws/chat/**")
                .setAllowedOrigins("*")
                .addInterceptors(new HttpHandshakeInterceptor());
    }
}
