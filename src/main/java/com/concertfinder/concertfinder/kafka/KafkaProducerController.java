package com.concertfinder.concertfinder.kafka;

import com.concertfinder.concertfinder.kafka.component.TestProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kafka")
public class KafkaProducerController {
    
    private final TestProducer testProducer;

    @PostMapping("/sending")
    public void create() {

        testProducer.create();
    }
}
