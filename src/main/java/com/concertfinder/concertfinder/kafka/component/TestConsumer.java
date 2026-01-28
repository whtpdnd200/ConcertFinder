package com.concertfinder.concertfinder.kafka.component;

import groovy.util.logging.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@lombok.extern.slf4j.Slf4j
@Component
@Slf4j
public class TestConsumer {

    @KafkaListener(topics = "topic", groupId = "group_1")
    public void listen(String message) {

        log.info("Received Messasge in group group_1:  {} ", message);
    }
}
