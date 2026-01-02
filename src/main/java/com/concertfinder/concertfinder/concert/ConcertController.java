package com.concertfinder.concertfinder.concert;

import com.concertfinder.concertfinder.concert.service.ConcertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/concert")
@RequiredArgsConstructor
public class ConcertController {

    private final ConcertService concertService;

    @GetMapping("/list")
    public String list() {

        return "concertfinder/concert/list";
    }

    @GetMapping(value = "/test", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String test() {

        return concertService.test();
    }
}
