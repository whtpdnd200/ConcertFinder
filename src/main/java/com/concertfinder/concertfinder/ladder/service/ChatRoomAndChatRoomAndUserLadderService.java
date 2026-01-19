package com.concertfinder.concertfinder.ladder.service;

import com.concertfinder.concertfinder.chat_message.service.ChatMessageService;
import com.concertfinder.concertfinder.chat_room.domain.ChatRoom;
import com.concertfinder.concertfinder.chat_room.service.ChatRoomService;
import com.concertfinder.concertfinder.chat_room_and_user.service.ChatRoomAndUserService;
import com.concertfinder.concertfinder.common.SimpleWebSocketHandler;
import com.concertfinder.concertfinder.last_chat.domain.LastChat;
import com.concertfinder.concertfinder.last_chat.service.LastChatService;
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

    private final SimpleWebSocketHandler simpleWebSocketHandler;

    private final ChatMessageService chatMessageService;

    private final LastChatService lastChatService;

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

    // 1:1 채팅방 삭제
    @Transactional
    public void deletePrivateChatRoomAndUser(long roomId) {

        deleteChatRoom(roomId);
        deleteAllChatRoomAndUser(roomId);
        chatMessageService.deleteMessage(roomId);
        simpleWebSocketHandler.deleteRoom(roomId);
    }

    public void deleteChatMessage(long roomId) {

        chatMessageService.deleteMessage(roomId);
    }

    public String getChatName(long roomId) {

        return chatRoomService.getChatRoomName(roomId);
    }

    public boolean isHost(long userId, long roomId) {

        return chatRoomAndUserService.isHost(userId, roomId);
    }

    public List<Long> getTop3ChatRoomIdList(long userId) {

        return chatRoomAndUserService.getTop3ChatRoomIdList(userId);
    }

    public List<Long> getChatRoomIdList(long userId) {

        return chatRoomAndUserService.getChatRoomIdList(userId);
    }

    public ChatRoom getChatRoom(long roomId) {

        return chatRoomService.getChatRoom(roomId);
    }

    public int getCurrentCount(long roomId) {

        return chatRoomAndUserService.getCurrentCount(roomId);
    }

    // 1:1 채팅방 생성 및 유저 저장 메서드
    @Transactional
    public long createPrivateChatRoom(long userId, long otherUserId) {

        Long chatRoomId = chatRoomService.getChatRoomId(userId, otherUserId);

        if(chatRoomId != null) {

            return chatRoomId;
        }

        long roomId = chatRoomService.insertChatRoom(null, null);
        chatRoomAndUserService.insertPrivateChatRoomAndUser(userId, roomId);
        chatRoomAndUserService.insertPrivateChatRoomAndUser(otherUserId, roomId);
        return roomId;
    }

    // 1:1 채팅 상대방 닉네임 반환
    public String getPrivateRoomNickname(long roomId, long userId) {

        return chatRoomAndUserService.getPrivateRoomNickname(roomId, userId);
    }

    // 마지막 읽은 채팅 내역 저장
    @Transactional
    public void updateLastChat(long roomId, long userId) {

        long lastId = chatMessageService.getLastChatMessageId(roomId);

        lastChatService.updateLastMessage(roomId, userId, lastId);
    }
}
