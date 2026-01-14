package com.concertfinder.concertfinder.ladder.service;

import com.concertfinder.concertfinder.chat_room.service.ChatRoomService;
import com.concertfinder.concertfinder.chat_room_and_user.service.ChatRoomAndUserService;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@lombok.extern.slf4j.Slf4j
@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomAndChatRoomAndUserLadderService {

    private final ChatRoomService chatRoomService;

    private final ChatRoomAndUserService chatRoomAndUserService;

    // 동행 글 작성한 유저의 채팅방 생성 및 입장 메서드
    @Transactional
    public void insertChatRoomAndChatAndUser(long accompanyId, long userId, String roomName) {

        long chatRoomId = chatRoomService.insertChatRoom(accompanyId, roomName);

        chatRoomAndUserService.insertChatRoomAndUser(userId, chatRoomId);
    }

    // 동행 신청유저 채팅방 입장 메서드
    public void insertChatRoomAndUser(long userId, long roomId) {

        chatRoomAndUserService.insertChatRoomAndUser(userId, roomId);
    }

    // 동행 신청 유저 채팅방 퇴장 메서드
    public void deleteChatRoomAndUser(long userId, long roomId) {

        chatRoomAndUserService.deleteChatRoomAndUser(userId, roomId);
    }

    // 동행 신청시 입장 할 채팅방의 번호 반환 메서드
    public long getRoomId(long accompanyId) {

        return chatRoomService.getRoomId(accompanyId);
    }

    public void deleteChatRoom(long roomId) {

        chatRoomService.deleteChatRoom(roomId);
    }

    public void deleteAllChatRoomAndUser(long roomId) {

        chatRoomAndUserService.deleteAllChatRoomAndUser(roomId);
    }

    public String getChatName(long roomId) {

        return chatRoomService.getChatRoomName(roomId);
    }
}
