package com.concertfinder.concertfinder.chat_room;

import com.concertfinder.concertfinder.chat_room.DTO.ChatRoomListDTO;
import com.concertfinder.concertfinder.common.DTO.ApiResponseDTO;
import com.concertfinder.concertfinder.ladder.service.AccompanyAndAccompanyCountLadderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRoomRestController {

    private final AccompanyAndAccompanyCountLadderService accompanyAndAccompanyCountLadderService;

    @GetMapping("/{userId}/list")
    public ResponseEntity<ApiResponseDTO<List<ChatRoomListDTO>>> getChatRoomList(@PathVariable long userId) {

        List<ChatRoomListDTO> chatRoomList = accompanyAndAccompanyCountLadderService.getChatRoomList(userId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.success("목록 출력 성공", chatRoomList));
    }
}
