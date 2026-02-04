package com.concertfinder.concertfinder.last_chat.service;

import com.concertfinder.concertfinder.last_chat.domain.LastChat;
import com.concertfinder.concertfinder.last_chat.repository.LastChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LastChatService {

    private final LastChatRepository lastChatRepository;


    // 채팅방에서 마지막으로 읽은 메시지 아이디 업데이트
    public void updateLastMessage(long roomId, long userId, long lastChatId) {

        LastChat lastChat = lastChatRepository.findByRoomIdAndUserId(roomId, userId).orElse(new LastChat(roomId, userId));

        lastChat = lastChat.toBuilder()
                .chatId(lastChatId)
                .build();

        try {
            lastChatRepository.save(lastChat);

        } catch(DataAccessException e) {

            throw new RuntimeException("서버 에러로인해 채팅방 내역을 저장하지 못했습니다!");
        }
    }

    // 사용자의 모든 마지막으로 읽은 채팅 삭제
    public void deleteLastChatUser(long userId) {

        List<LastChat> lastChats = lastChatRepository.findAllByUserId(userId);

        for(LastChat l : lastChats) {

            try {
                lastChatRepository.delete(l);
            } catch(DataAccessException e) {

                throw new RuntimeException("서버 에러 발생!");
            }
        }
    }

    // 단일 채팅방의 마지막으로 읽은 메시지 삭제
    public void deleteLastChat(long roomId) {

        List<LastChat> lastChatList = lastChatRepository.findAllByRoomId(roomId);

        for(LastChat l : lastChatList) {

            try {
                lastChatRepository.delete(l);
            } catch(DataAccessException e) {

                throw new RuntimeException("서버 에러 발생!");
            }
        }
    }

    // 단일 채팅방의 마지막으로 읽은 메시지 아이디 반환
    public Long getLastChatId(long roomId, long userId) {

        return lastChatRepository.findByRoomIdAndUserId(roomId, userId).map(LastChat::getChatId).orElse(null);
    }
}
