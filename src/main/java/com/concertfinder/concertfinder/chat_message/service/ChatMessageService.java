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
import org.springframework.data.domain.SliceImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    private final UserService userService;

    private final LastChatService lastChatService;

    // 메세지 db 저장 메서드
    @Async("messageSaveExecutor")
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

            log.error("메시지 저장 실패! roomId : {}, userId : {}, error : {}", sendMessageDTO.getRoomId(), sendMessageDTO.getUserId(), e.getMessage());
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



//    // 메시지 출력 메서드
//    public Slice<MessageListDTO> getMessageList(long roomId, long userId, Pageable pageable) {
//
//        long lastId = lastChatService.getLastChatId(roomId, userId);
//
//        Slice<ChatMessage> chatMessages = null;
//
//        if(lastId == 0l) {
//
//            chatMessages = chatMessageRepository.findAllByRoomIdOrderByIdDesc(roomId, PageRequest.of(0, 15));
//        } else {
//
//            chatMessages = chatMessageRepository.findByRoomIdAndIdGreaterThanEqualOrderByIdAsc(roomId, lastId, PageRequest.of(0, 100));
//        }
//
//        return chatMessages.map(entity -> MessageListDTO.builder()
//                .id(entity.getId())
//                .type(entity.getMessageType())
//                .userNickname(userService.getNickname(entity.getUserId()))
//                .content(entity.getContent())
//                .reverse(lastId == 0l)
//                .createdAt(entity.getCreatedAt())
//                .build());
//    }

    // 메시지 목록 출력 메서드
    public Slice<MessageListDTO> getMessageList(long roomId, long userId, Pageable pageable) {
        // 1. 마지막으로 읽은 ID 조회
        Long lastId = lastChatService.getLastChatId(roomId, userId);

        log.info("마지막 id {}", lastId);
        List<ChatMessage> combinedList = new ArrayList<>();
        boolean hasNext = false;

        if (lastId == null) {
            // 읽은 기록이 없는 경우 최신 메시지 15개만 가져옴

            Slice<ChatMessage> latest = chatMessageRepository.findAllByRoomIdOrderByIdDesc(roomId, PageRequest.of(0, 15));
            combinedList.addAll(latest.getContent());
            hasNext = latest.hasNext();
        } else {
            // 중간 지점이 있는 경우
            // 과거 메시지 15개 (DESC로 가져와서 뒤집기)
            Slice<ChatMessage> before = chatMessageRepository.findByRoomIdAndIdLessThanEqualOrderByIdDesc(roomId, lastId, PageRequest.of(0, 15));
            List<ChatMessage> beforeContent = new ArrayList<>(before.getContent());
            Collections.reverse(beforeContent); // 오래된 순서(ASC)로 정렬

            // 이후 최신 메시지들 (ASC)
            Slice<ChatMessage> next = chatMessageRepository.findByRoomIdAndIdGreaterThanOrderByIdAsc(roomId, lastId, PageRequest.of(0, 50));

            combinedList.addAll(beforeContent); // 과거 추가

            // "여기까지 읽었습니다" 시스템 메시지 삽입
            if (!next.getContent().isEmpty()) {
                combinedList.add(ChatMessage.builder()
                        .id(-1L) // 실제 DB ID가 아닌 구분용 ID
                        .messageType("SYSTEM_LINE")
                        .content("여기까지 읽었습니다")
                        .build());
            }

            combinedList.addAll(next.getContent());
            hasNext = next.hasNext();
        }

        // ChatMessage 리스트를 MessageListDTO 리스트로 변환
        List<MessageListDTO> dtoList = combinedList.stream()
                .map(entity -> MessageListDTO.builder()
                        .id(entity.getId())
                        .type(entity.getMessageType())
                        .userNickname(userService.getNickname(entity.getUserId()))
                        .content(entity.getContent())
                        .reverse(lastId == 0L) // 처음 들어올 때만 아래서 위로 출력
                        .createdAt(entity.getCreatedAt())
                        .build())
                .toList();

        // SliceImpl을 통해 Slice 타입으로 반환
        return new SliceImpl<>(dtoList, pageable, hasNext);
    }

    // 다음 메시지 출력 메서드
    public Slice<MessageListDTO> getNextMessageList(long roomId, long nextId) {

        Slice<ChatMessage> chatMessages = chatMessageRepository.findByRoomIdAndIdGreaterThanOrderByIdAsc(roomId, nextId, PageRequest.of(0, 15));

        return chatMessages.map(entity -> MessageListDTO.builder()
                .id(entity.getId())
                .type(entity.getMessageType())
                .userNickname(userService.getNickname(entity.getUserId()))
                .content(entity.getContent())
                .reverse(false)
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
                .reverse(false)
                .createdAt(entity.getCreatedAt())
                .build());
    }

    public Long getLastChatMessageId(long roomId) {

        Optional<ChatMessage> optionalChatMessage = chatMessageRepository.findFirstByRoomIdOrderByIdDesc(roomId);


        return optionalChatMessage.map(ChatMessage::getId).orElse(null);
    }

    @Transactional
    public void updateLastChat(long roomId, long userId) {

        Long lastId = getLastChatMessageId(roomId);

        if(lastId != null) {

            lastChatService.updateLastMessage(roomId, userId, lastId);
        }
    }
}
