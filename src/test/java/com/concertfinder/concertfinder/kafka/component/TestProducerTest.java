package com.concertfinder.concertfinder.kafka.component;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TestProducerTest {

    @Autowired
    private TestProducer testProducer;

    @Transactional
    @Test
    public void test() {

        testProducer.create();
    }
}