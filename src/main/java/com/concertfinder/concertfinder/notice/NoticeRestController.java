package com.concertfinder.concertfinder.notice;

import com.concertfinder.concertfinder.notice.DTO.SseSendRequestDTO;
import com.concertfinder.concertfinder.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sse")
public class NoticeRestController {

    private final NoticeService noticeService;

    @GetMapping(value = "/subscribe/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable Long userId) {

        return noticeService.subscribe(userId);
    }

    @PostMapping("/send/{userId}")
    public void sendAlarm(@PathVariable Long userId, @RequestBody SseSendRequestDTO sseSendRequest) {

        noticeService.sendToClient(userId, sseSendRequest.getEventName(), sseSendRequest.getData());
    }
}
