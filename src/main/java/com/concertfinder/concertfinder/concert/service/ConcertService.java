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

    public ResponsesDTO getResponseDTO(ResponsesDTO responsesDTO, int rows) {

        int size = responsesDTO.getLists().size();

        if(responsesDTO != null && responsesDTO.getLists() != null) {
            if(size > rows) {
                responsesDTO.setHasNext(true);
                responsesDTO.getLists().remove(size - 1);
            } else {
                responsesDTO.setHasNext(false);
            }
        }

        return responsesDTO;
    }

    // 기본 화면의 콘서트 목록 출력 메서드
    public ResponsesDTO getList(byte code, Integer page) {

        if(page == null) {
            page = 1;
        }

        final int cPage = page;

        final int rows = 8;

        ResponsesDTO responsesDTO = kopisWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/pblprfr")
                        .queryParam("service", kopisProperties.getKey())
                        .queryParam("stdate", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                        .queryParam("eddate", LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                        .queryParam("cpage", cPage)
                        .queryParam("rows", rows + 1)
                        .queryParam("shcate", "CCCD")
                        .queryParam("signgucode", code)
                        .build())
                .retrieve()
                .bodyToMono(ResponsesDTO.class)
                .block();

        return getResponseDTO(responsesDTO, rows);
    }
}
