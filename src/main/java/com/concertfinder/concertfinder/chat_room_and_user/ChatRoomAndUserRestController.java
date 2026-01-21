package com.concertfinder.concertfinder.chat_room_and_user;

import com.concertfinder.concertfinder.chat_room_and_user.DTO.ChatUserInfoDTO;
import com.concertfinder.concertfinder.chat_room_and_user.service.ChatRoomAndUserService;
import com.concertfinder.concertfinder.common.DTO.ApiResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/room")
public class ChatRoomAndUserRestController {

    private final ChatRoomAndUserService chatRoomAndUserService;


    @GetMapping("/users/{roomId}")
    public ResponseEntity<ApiResponseDTO<List<ChatUserInfoDTO>>> getUsers(@PathVariable Long roomId) {


        List<ChatUserInfoDTO> userInfoList = chatRoomAndUserService.getUserInfoList(roomId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.success("유저 목록 출력 성공", userInfoList));
    }
}
