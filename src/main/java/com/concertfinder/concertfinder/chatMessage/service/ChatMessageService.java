package com.concertfinder.concertfinder.chatMessage.service;

import com.concertfinder.concertfinder.chatMessage.DTO.SendMessageDTO;
import com.concertfinder.concertfinder.chatMessage.domain.ChatMessage;
import com.concertfinder.concertfinder.chatMessage.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    // 메세지 db 저장 메서드
    public void insertMessage(SendMessageDTO sendMessageDTO) {

        ChatMessage chatMessage = ChatMessage.builder()
                .roomId(sendMessageDTO.getRoomId())
                .userId(sendMessageDTO.getUserId())
                .messageType(sendMessageDTO.getMessageType())
                .content(sendMessageDTO.getContent())
                .build();

        try {
            chatMessageRepository.save(chatMessage);

        } catch(DataAccessException e) {

            throw new RuntimeException("메시지를 저장하는 중 에러가 발생 했습니다!");
        }
    }

    public void deleteMessage(long roomId) {

        List<ChatMessage> chatMessages = chatMessageRepository.findAllByRoomId(roomId);

        for(ChatMessage c : chatMessages) {

            try {
                chatMessageRepository.delete(c);

            } catch(DataAccessException e) {

                throw new RuntimeException("서버 에러로 채팅메세지를 삭제 하지 못했습니다 잠시 후 다시 시도 해주세요!");
            }
        }
    }
}
