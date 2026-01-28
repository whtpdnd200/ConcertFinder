package com.concertfinder.concertfinder.kafka.service;

import com.concertfinder.concertfinder.kafka.DTO.NoticeDTO;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@lombok.extern.slf4j.Slf4j
@Service
@RequiredArgsConstructor
@Slf4j
public class ConsumerService {


    @KafkaListener(topics = "notice", groupId = "group_1")
    public void listen(NoticeDTO noticeDTO) {

        log.info("컨슈머가 메시지 전달 : {} ", noticeDTO);

        // sse로 전달하는 로직
    }
}
