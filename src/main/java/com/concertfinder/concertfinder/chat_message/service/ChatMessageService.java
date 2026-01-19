package com.concertfinder.concertfinder.chat_message.service;

import com.concertfinder.concertfinder.chat_message.DTO.MessageListDTO;
import com.concertfinder.concertfinder.chat_message.DTO.SendMessageDTO;
import com.concertfinder.concertfinder.chat_message.domain.ChatMessage;
import com.concertfinder.concertfinder.chat_message.repository.ChatMessageRepository;
import com.concertfinder.concertfinder.last_chat.service.LastChatService;
import com.concertfinder.concertfinder.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    private final UserService userService;

    private final LastChatService lastChatService;

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

    // 메시지 출력 메서드
    public Slice<MessageListDTO> getMessageList(long roomId, Pageable pageable) {


        Slice<ChatMessage> chatMessages = chatMessageRepository.findAllByRoomIdOrderByIdDesc(roomId, PageRequest.of(0, 15));

        return chatMessages.map(entity -> MessageListDTO.builder()
                .id(entity.getId())
                .type(entity.getMessageType())
                .userNickname(userService.getNickname(entity.getUserId()))
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .build());
    }

    // 이전 메시지 출력 메서드
    public Slice<MessageListDTO> getBeforeMessageList(long roomId, long lastId, Pageable pageable) {

        Slice<ChatMessage> beforeMessages = chatMessageRepository.findByRoomIdAndIdLessThanOrderByIdDesc(roomId, lastId, PageRequest.of(0, 15));

        return beforeMessages.map(entity -> MessageListDTO.builder()
                .id(entity.getId())
                .type(entity.getMessageType())
                .userNickname(userService.getNickname(entity.getUserId()))
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .build());
    }

    public long getLastChatMessageId(long roomId) {

        Optional<ChatMessage> optionalChatMessage = chatMessageRepository.findFirstByRoomIdOrderByIdDesc(roomId);

        if(!optionalChatMessage.isPresent()) {

            throw new NoSuchElementException("서버 에러 발생!");
        }

        return optionalChatMessage.get().getId();
    }

    @Transactional
    public void updateLastChat(long roomId, long userId) {

        long lastId = getLastChatMessageId(roomId);

        lastChatService.updateLastMessage(roomId, userId, lastId);
    }
}
