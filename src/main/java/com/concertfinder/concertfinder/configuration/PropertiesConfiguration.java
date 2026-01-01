package com.concertfinder.concertfinder.configuration;

import com.concertfinder.concertfinder.configuration.properties.KopisProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties({KopisProperties.class})
@RequiredArgsConstructor
public class PropertiesConfiguration {

    private final KopisProperties kopisProperties;

    // kopis용 API 호출 객체 베이스 생성
    @Bean
    public WebClient kopisWebClient() {

        return WebClient.builder()
                .baseUrl(kopisProperties.getBaseUrl())
                .defaultHeaders(headers -> {
                    headers.add("Accept", "application/xml");
                    headers.add("Content-Type", "application/xml");
                })
                .build();
    }
}
