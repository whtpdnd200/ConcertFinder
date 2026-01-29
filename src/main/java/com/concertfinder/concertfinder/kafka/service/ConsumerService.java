package com.concertfinder.concertfinder.kafka.service;

import com.concertfinder.concertfinder.kafka.DTO.NoticeDTO;
import com.concertfinder.concertfinder.notice.DTO.NoticeSendDTO;
import com.concertfinder.concertfinder.notice.service.NoticeService;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@lombok.extern.slf4j.Slf4j
@Service
@RequiredArgsConstructor
@Slf4j
public class ConsumerService {

    private final NoticeService noticeService;

    @Transactional
    @KafkaListener(topics = "notice", groupId = "group_1")
    public void listen(NoticeDTO noticeDTO) {

        log.info("컨슈머가 메시지 전달 : {} ", noticeDTO);
        Long id = noticeService.insertNotice(noticeDTO);

        NoticeSendDTO noticeSendDTO = NoticeSendDTO.builder()
                .id(id)
                .receiverId(noticeDTO.getReceiverId())
                .noticeType(noticeDTO.getNoticeType())
                .message(noticeDTO.getMessage())
                .url(noticeDTO.getUrl())
                .build();

        // sse로 전달하는 로직

        noticeService.sendToClient(noticeSendDTO.getReceiverId()
                        , noticeSendDTO.getNoticeType()
                                  , noticeSendDTO);
    }
}
