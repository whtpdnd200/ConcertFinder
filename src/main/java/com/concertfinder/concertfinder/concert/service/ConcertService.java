package com.concertfinder.concertfinder.concert.service;

import com.concertfinder.concertfinder.concert.DTO.ResponsesDTO;
import com.concertfinder.concertfinder.configuration.properties.KopisProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final WebClient kopisWebClient;
    private final KopisProperties kopisProperties;

    public ResponsesDTO getList(byte code, Integer page) {


        if(page == null) {
            page = 1;
        }

        final int cpage = page;

        return kopisWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/pblprfr")
                        .queryParam("service", kopisProperties.getKey())
                        .queryParam("stdate", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                        .queryParam("eddate", LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                        .queryParam("cpage", cpage)
                        .queryParam("rows", 8)
                        .queryParam("shcate", "CCCD")
                        .queryParam("signgucode", code)
                        .build())
                .retrieve()
                .bodyToMono(ResponsesDTO.class)
                .block();
    }
}
