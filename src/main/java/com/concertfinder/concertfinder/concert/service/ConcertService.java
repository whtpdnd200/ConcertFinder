package com.concertfinder.concertfinder.concert.service;

import com.concertfinder.concertfinder.concert.DTO.areaDTO.ResponsesAreaDTO;
import com.concertfinder.concertfinder.concert.DTO.concertListDTO.ResponsesDTO;
import com.concertfinder.concertfinder.concert.DTO.infoDTO.ResponsesInfoDTO;
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


    // 다음 페이지가 있는지를 판단하는 hasNext값을 추가 해주는 메서드
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
                        .queryParam("eddate", LocalDate.now().plusYears(1).format(DateTimeFormatter.ofPattern("yyyyMMdd")))
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

    public ResponsesInfoDTO getConcertInfo(String concertId) {

        ResponsesInfoDTO responsesInfoDTO = kopisWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/pblprfr/{concertId}")
                        .queryParam("service", kopisProperties.getKey())
                        .build(concertId))
                .retrieve()
                .bodyToMono(ResponsesInfoDTO.class)
                .block();

        String areaId = responsesInfoDTO.getInfoDTO().getAreaCode();
        responsesInfoDTO.getInfoDTO().setAreaInfo(getAreaInfo(areaId).getAreaInfoDTO());
        return responsesInfoDTO;
    }

    public ResponsesAreaDTO getAreaInfo(String areaCode) {

        return kopisWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/prfplc/{areaId}")
                        .queryParam("service", kopisProperties.getKey())
                        .build(areaCode))
                .retrieve()
                .bodyToMono(ResponsesAreaDTO.class)
                .block();
    }
}
