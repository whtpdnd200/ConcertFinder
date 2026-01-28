package com.concertfinder.concertfinder.kafka.service;

import com.concertfinder.concertfinder.kafka.DTO.NoticeDTO;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@lombok.extern.slf4j.Slf4j
@Service
@RequiredArgsConstructor
@Slf4j
public class ProducerService {

    private final KafkaTemplate<String, NoticeDTO> kafkaTemplate;


    public void create(NoticeDTO noticeDTO) {

        log.info("프로듀서로 알림 전송됨 : {} ", noticeDTO);
        kafkaTemplate.send("notice", noticeDTO);
    }
}
