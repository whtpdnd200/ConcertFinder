package com.concertfinder.concertfinder.concert;

import com.concertfinder.concertfinder.concert.service.ConcertService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/concert")
@RequiredArgsConstructor
public class ConcertRestController {

    private final ConcertService concertService;


}
