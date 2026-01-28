package com.concertfinder.concertfinder.notice.service;

import groovy.util.logging.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@lombok.extern.slf4j.Slf4j
@Service
@Slf4j
public class NoticeService {

    private final Map<Long, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();

    // 구독 기능
    public SseEmitter subscribe(Long userId) {

        long timeout = 1000L * 60 * 60; // sse emitter 연결 시간, 1시간

        // sseEmitter 저장
        SseEmitter sseEmitter = new SseEmitter(timeout);
        sseEmitterMap.put(userId, sseEmitter);


        // sseEmitter complete 처리
        sseEmitter.onCompletion(() -> sseEmitterMap.remove(userId));
        // sseEmitter timeout 발생
        sseEmitter.onTimeout(sseEmitter::complete);
        // sseEmitter error 발생
        sseEmitter.onError(throwable -> sseEmitter.complete());

        // connect event로 message 발생
        sendToClient(userId, "connect", "sse connect");

        return sseEmitter;
    }

    // 클라이언트에 메시지 전송
    public void sendToClient(Long userId, String eventName, Object data) {
        SseEmitter sseEmitter = sseEmitterMap.get(userId);
        try {

            sseEmitter.send(SseEmitter
                            .event()
                            .id(String.valueOf(userId))
                            .name(eventName)
                            .data(data));

        } catch (IOException e) {

            sseEmitterMap.remove(userId);
            sseEmitter.completeWithError(e);
        }
    }
}
