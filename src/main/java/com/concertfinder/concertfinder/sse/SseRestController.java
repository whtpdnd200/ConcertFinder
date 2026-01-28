package com.concertfinder.concertfinder.sse;

import com.concertfinder.concertfinder.sse.DTO.SseSendRequestDTO;
import com.concertfinder.concertfinder.sse.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class SseRestController {

    private final SseService sseService;

    @GetMapping("/subscribe/{id}")
    public SseEmitter subscribe(@PathVariable String id) {

        return sseService.subscribe(id);
    }

    @PostMapping("/send/{id}")
    public void sendAlarm(@PathVariable String id, @RequestBody SseSendRequestDTO sseSendRequest) {

        sseService.sendToClient(id, sseSendRequest.getEventName(), sseSendRequest.getData());
    }
}
