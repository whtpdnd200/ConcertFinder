package com.concertfinder.concertfinder.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "group_1");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        // 오프셋을 찾을 수 없을 때 가장 최신의 메시지부터 읽기 시작하도록 설정
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        // 아래 두줄 코드는 토픽에 대해서 auto commit으로 100초로 간격을 설정
        // config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        // config.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 100000);

        return new DefaultKafkaConsumerFactory<>(config);
    }

    // Kafka 리스터 컨테이너 팩토리 정의, Kafka 리스너를 컨테이너화하여 실행
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
