package com.concertfinder.concertfinder.concert;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/concert")
public class ConcertController {


    @GetMapping("/list")
    public String list() {

        return "concertfinder/concert/list";
    }
}
