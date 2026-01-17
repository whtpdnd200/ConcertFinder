package com.concertfinder.concertfinder.chat_room.service;

import com.concertfinder.concertfinder.chat_room.DTO.ChatRoomInfoDTO;
import com.concertfinder.concertfinder.chat_room.domain.ChatRoom;
import com.concertfinder.concertfinder.chat_room.repository.ChatRoomRepository;
import com.concertfinder.concertfinder.chat_room_and_user.domain.ChatRoomAndUser;
import com.concertfinder.concertfinder.chat_room_and_user.service.ChatRoomAndUserService;
import com.concertfinder.concertfinder.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    private final ChatRoomAndUserService chatRoomAndUserService;


    // 채팅방 번호 유저 번호 저장 메서드
    public long insertChatRoom(Long accompanyId, String roomName) {

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

    public Long getChatRoomId(long userId, long otherUserId) {

        return chatRoomRepository.findRoomIdByNativeQuery(userId, otherUserId).orElse(null);
    }

    public ChatRoom getChatRoom(long roomId) {

        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findById(roomId);

        if(!optionalChatRoom.isPresent()) {

            throw new NoSuchElementException("채팅방 정보 없음!");
        }

        return optionalChatRoom.get();
    }

    public String getChatRoomName(long roomId) {

        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findById(roomId);

        if(!optionalChatRoom.isPresent()) {

            throw new NoSuchElementException("채팅방 정보를 불러 올 수 없습니다!");
        }

        return optionalChatRoom.get().getRoomName();
    }

    // 채팅방 정보 및 참여중인 유저 정보 DTO 반환 메서드
    public ChatRoomInfoDTO getChatRoomInfo(long roomId, long userId) {

        log.info("방 번호 : {}" , roomId);
        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findById(roomId);

        if(!optionalChatRoom.isPresent()) {

            throw new NoSuchElementException("채팅방 정보를 불러오지 못했습니다!");
        }

        ChatRoom chatRoom = optionalChatRoom.get();

        log.info("채팅방 {} ", chatRoom);

        ChatRoomInfoDTO chatRoomInfoDTO = ChatRoomInfoDTO.builder()
                .chatRoomId(roomId)
                .accompanyId(chatRoom.getAccompanyId())
                .roomName(chatRoom.getRoomName())
                .isHost(chatRoomAndUserService.isHost(userId, roomId))
                .currentCount(chatRoomAndUserService.getCurrentCount(roomId))
                .build();

        log.info("채팅방 DTO : {} ", chatRoomInfoDTO);
        return chatRoomInfoDTO;
    }
}
