package com.concertfinder.concertfinder.chat_room.service;

import com.concertfinder.concertfinder.chat_room.domain.ChatRoom;
import com.concertfinder.concertfinder.chat_room.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    // 채팅방 번호 유저 번호 저장 메서드
    public long insertChatRoom(long accompanyId, String roomName) {

        ChatRoom chatRoom = ChatRoom.builder()
                .accompanyId(accompanyId)
                .roomName(roomName)
                .build();

        try {
            return chatRoomRepository.save(chatRoom).getId();

        } catch(DataAccessException e) {

            throw new RuntimeException("서버 에러로인해 채팅방을 생성하지 못했습니다 잠시 후 다시 시도해주세요!");
        }
    }

    // 채팅방 번호 반환 메서드
    public long getRoomId(long accompanyId) {

        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findByAccompanyId(accompanyId);

        if(!optionalChatRoom.isPresent()) {

            throw new NoSuchElementException("채팅방 정보를 불러 올 수 없습니다!");
        }

        return optionalChatRoom.get().getId();
    }

    public void deleteChatRoom(long roomId) {

        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findById(roomId);

        if(!optionalChatRoom.isPresent()) {

            throw new NoSuchElementException("채팅방 정보를 찾을 수 없습니다!");
        }

        ChatRoom chatRoom = optionalChatRoom.get();

        try {
            chatRoomRepository.delete(chatRoom);
        } catch(DataAccessException e) {

            throw new RuntimeException("서버 에러로인해 채팅방을 삭제 할 수 없습니다! 잠시 후 다시 시도해주세요!");
        }
    }
}
