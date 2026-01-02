package com.concertfinder.concertfinder.concert.service;

import com.concertfinder.concertfinder.SidoCode.service.SidoCodeService;
import com.concertfinder.concertfinder.concert.DTO.ParentsResponsesDTO;
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
    private final SidoCodeService sidoCodeService;

    public ParentsResponsesDTO getResponseDTO(ParentsResponsesDTO responsesDTO, int rows) {

        if(responsesDTO != null && responsesDTO.getLists() != null) {
            int size = responsesDTO.getLists().size();
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
    public ParentsResponsesDTO getList(byte code, Integer page) {

        if(page == null) {
            page = 1;
        }

        final int cPage = page;

        final int rows = 8;

        ParentsResponsesDTO responsesDTO = kopisWebClient.get()
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
                .bodyToMono(ParentsResponsesDTO.class)
                .block();

        return getResponseDTO(responsesDTO, rows);
    }

    // 검색 키워드와 지역에 일치하는 콘서트 목록 출력

}
