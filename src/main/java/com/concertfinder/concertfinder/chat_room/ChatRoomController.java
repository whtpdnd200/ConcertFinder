package com.concertfinder.concertfinder.chat_room;

import com.concertfinder.concertfinder.chat_room.service.ChatRoomService;
import com.concertfinder.concertfinder.user.DTO.LoginUserDTO;
import com.concertfinder.concertfinder.user.DTO.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;


    @GetMapping("/room/{roomId}")
    public String chatRoom(@PathVariable long roomId
                           , @AuthenticationPrincipal PrincipalDetails principal
                          , Model model) {

        LoginUserDTO loginUserDTO = principal.getLoginUserDTO();
        model.addAttribute("roomId", roomId);
        model.addAttribute("chatRoomInfo", chatRoomService.getChatRoomInfo(roomId, loginUserDTO.getId()));

        return "concertfinder/chat/chat_room";
    }
}
