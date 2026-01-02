package com.concertfinder.concertfinder.concert.service;

import com.concertfinder.concertfinder.configuration.properties.KopisProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final WebClient kopisWebClient;
    private final KopisProperties kopisProperties;

    public String test() {

        return kopisWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/pblprfr")
                        .queryParam("service", kopisProperties.getKey())
                        .queryParam("stdate", "20260101")
                        .queryParam("eddate", "20260115")
                        .queryParam("cpage", 1)
                        .queryParam("rows", 5)
                        .build())
                .accept(MediaType.valueOf(MediaType.APPLICATION_XML_VALUE))
                .retrieve()
                .bodyToMono(String.class)
                .block();

    }
}
