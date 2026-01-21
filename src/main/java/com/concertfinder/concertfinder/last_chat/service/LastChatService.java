package com.concertfinder.concertfinder.last_chat.service;

import com.concertfinder.concertfinder.last_chat.domain.LastChat;
import com.concertfinder.concertfinder.last_chat.repository.LastChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LastChatService {

    private final LastChatRepository lastChatRepository;


    public void updateLastMessage(long roomId, long userId, long lastChatId) {

        LastChat lastChat = lastChatRepository.findByRoomIdAndUserId(roomId, userId).orElse(new LastChat(roomId, userId));

        lastChat = lastChat.toBuilder()
                .lastChatId(lastChatId)
                .build();

        try {
            lastChatRepository.save(lastChat);

        } catch(DataAccessException e) {

            throw new RuntimeException("서버 에러로인해 채팅방 내역을 저장하지 못했습니다!");
        }
    }

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

    public Long getLastChatId(long roomId, long userId) {

        return lastChatRepository.findByRoomIdAndUserId(roomId, userId).map(LastChat::getLastChatId).orElse(null);
    }
}
