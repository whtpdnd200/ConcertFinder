package com.concertfinder.concertfinder.chat_test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatController {

    @GetMapping("/chat-html")
    public String testChat() {

        return "concertfinder/chat/chat-test";
    }
}
