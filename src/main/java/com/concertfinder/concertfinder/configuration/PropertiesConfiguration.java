package com.concertfinder.concertfinder.configuration;

import com.concertfinder.concertfinder.configuration.properties.KopisProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.xml.Jaxb2XmlDecoder;
import org.springframework.http.codec.xml.Jaxb2XmlEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties({KopisProperties.class})
@RequiredArgsConstructor
// 웹 클라이언트 객체를 스프링 빈으로 등록하고 사용하기 위해 만드는 클래스
public class PropertiesConfiguration {

    private final KopisProperties kopisProperties;

    // kopis용 API 호출 객체 베이스 생성
    @Bean
    public WebClient kopisWebClient() {

        return WebClient.builder()
                .baseUrl(kopisProperties.getBaseUrl())
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(clientCodecConfigurer ->
                                clientCodecConfigurer
                                        .defaultCodecs().jaxb2Encoder(new Jaxb2XmlEncoder()))
                        .codecs(clientCodecConfigurer ->
                                clientCodecConfigurer
                                        .defaultCodecs().jaxb2Decoder(new Jaxb2XmlDecoder()))
                        .build())
                .build();
    }
}
