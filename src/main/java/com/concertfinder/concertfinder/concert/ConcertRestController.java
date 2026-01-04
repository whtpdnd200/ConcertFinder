package com.concertfinder.concertfinder.concert;

import com.concertfinder.concertfinder.concert.DTO.areaDTO.ResponsesAreaDTO;
import com.concertfinder.concertfinder.concert.DTO.infoDTO.ResponsesInfoDTO;
import com.concertfinder.concertfinder.concert.service.ConcertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/concert")
@RequiredArgsConstructor
public class ConcertRestController {

    private final ConcertService concertService;


    @GetMapping(value = "/test/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponsesInfoDTO test(@PathVariable String id) {

        return concertService.getConcertInfo(id);
    }

    @GetMapping(value = "/areaTest/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponsesAreaDTO areaTest(@PathVariable String id) {

        return concertService.getAreaInfo(id);
    }
}
