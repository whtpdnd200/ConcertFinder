package com.concertfinder.concertfinder.chat_room;

import com.concertfinder.concertfinder.chat_room.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;


    @GetMapping("/room/{roomId}")
    public String chatRoom(@PathVariable long roomId) {

        return "concertfinder/chat/chat_room";
    }
}
