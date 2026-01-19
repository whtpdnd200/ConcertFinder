package com.concertfinder.concertfinder.chat_message;

import com.concertfinder.concertfinder.chat_message.DTO.MessageListDTO;
import com.concertfinder.concertfinder.chat_message.service.ChatMessageService;
import com.concertfinder.concertfinder.common.DTO.ApiResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
public class ChatMessageRestController {

    private final ChatMessageService chatMessageService;


    @GetMapping("/{roomId}")
    public ResponseEntity<ApiResponseDTO<Slice<MessageListDTO>>> getMessageList(@PathVariable long roomId, Pageable pageable) {

        Slice<MessageListDTO> messageList = chatMessageService.getMessageList(roomId, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.success("채팅 목록 출력 성공", messageList));
    }

    @GetMapping("/{roomId}/{lastId}")
    public ResponseEntity<ApiResponseDTO<Slice<MessageListDTO>>> getBeforeMessageList(@PathVariable long roomId
                                                                                     , @PathVariable long lastId
                                                                                     , Pageable pageable) {
        Slice<MessageListDTO> beforeMessageList = chatMessageService.getBeforeMessageList(roomId, lastId, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.success("이전 채팅 목록 출력 성공", beforeMessageList));
    }
}
