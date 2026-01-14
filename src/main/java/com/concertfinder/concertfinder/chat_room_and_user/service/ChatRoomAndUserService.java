package com.concertfinder.concertfinder.chat_room_and_user.service;

import com.concertfinder.concertfinder.chat_room_and_user.domain.ChatRoomAndUser;
import com.concertfinder.concertfinder.chat_room_and_user.repository.ChatRoomAndUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomAndUserService {

    private final ChatRoomAndUserRepository chatRoomAndUserRepository;

    // 동행 신청 및 동행 글 생성한 사용자의 채팅방 입장 메서드
    public void insertChatRoomAndUser(long userId, long roomId) {

        boolean isHost = false;

        if(!chatRoomAndUserRepository.existsByRoomId(roomId)) {
            isHost = true;
        }

        ChatRoomAndUser chatRoomAndUser = ChatRoomAndUser.builder()
                .userId(userId)
                .roomId(roomId)
                .isHost(isHost)
                .build();

        try {
            chatRoomAndUserRepository.save(chatRoomAndUser);
        } catch(DataAccessException e) {

            throw new RuntimeException("서버 에러로 인해 채팅방을 생성하지 못했습니다 잠시 후 다시 시도해주세요!");
        }
    }

    // 동행 신청 취소 시 채팅방 퇴장 메서드
    public void deleteChatRoomAndUser(long userId, long roomId) {

        Optional<ChatRoomAndUser> optionalChatRoomAndUser = chatRoomAndUserRepository.findByUserIdAndRoomId(userId, roomId);

        if(!optionalChatRoomAndUser.isPresent()) {

            throw new NoSuchElementException("유저 정보를 찾을 수 없습니다!");
        }

        ChatRoomAndUser chatRoomAndUser = optionalChatRoomAndUser.get();

        try {

            chatRoomAndUserRepository.delete(chatRoomAndUser);
        } catch(DataAccessException e) {

            throw new RuntimeException("서버 에러로인해 채팅방을 나갈 수 없습니다 잠시 후 다시 시도해주세요!");
        }
    }

    // 게시글 삭제시 채팅방 삭제 메서드
    public void deleteAllChatRoomAndUser(long roomId) {

        List<ChatRoomAndUser> chatRoomAndUsers = chatRoomAndUserRepository.findAllByRoomId(roomId);

        for(ChatRoomAndUser c : chatRoomAndUsers) {

            deleteChatRoomAndUser(c.getUserId(), c.getRoomId());
        }
    }
}
