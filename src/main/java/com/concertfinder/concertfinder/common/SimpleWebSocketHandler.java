package com.concertfinder.concertfinder.common;

import groovy.util.logging.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@lombok.extern.slf4j.Slf4j
@Component
@Slf4j
public class SimpleWebSocketHandler extends TextWebSocketHandler {

    private Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        sessions.add(session);
        log.info("connected : {} ", session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        // 규격: [0]방번호 | [1]타입 | [2]닉네임 | [3]메시지

        String payload = message.getPayload();

        // |의 뒤가 공백이어도 전부 스플릿
        String[] parts = payload.split("\\|", -1);

        StringBuilder sb = new StringBuilder(payload);

        if(parts[1].equals("CONNECT")) {

            sb.append("님이 입장 하셧습니다.");

        } else if(parts[1].equals("DISCONNECT")) {

            sb.append("님이 퇴장 하셧습니다.");
        }

        log.info("sb : {} ", sb.toString());

        for(WebSocketSession s : sessions) {

            if(s.isOpen()) {
                s.sendMessage(new TextMessage(sb.toString()));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        sessions.remove(session);
    }
}
