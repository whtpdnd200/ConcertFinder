package com.concertfinder.concertfinder.concert.service;

import com.concertfinder.concertfinder.SidoCode.service.SidoCodeService;
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
    private final SidoCodeService sidoCodeService;

    public ResponsesDTO getResponseDTO(ResponsesDTO responsesDTO, int rows) {

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
    public ResponsesDTO getList(String code
                                , Integer page
                                , String areaCode
                                , String keyword) {

        if(page == null) {
            page = 1;
        }

        if(areaCode != null) {
            code = areaCode;
        }

        if(areaCode != null && areaCode.equals("00")) {
            code = "";
        }

        final String fCode = code;

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
                        .queryParam("signgucode", fCode)
                        .queryParam("shprfnm", keyword)
                        .build())
                .retrieve()
                .bodyToMono(ResponsesDTO.class)
                .block();

        return getResponseDTO(responsesDTO, rows);
    }


}
