package com.concertfinder.concertfinder.chat_room_and_user.service;

import com.concertfinder.concertfinder.chat_room_and_user.DTO.ChatUserInfoDTO;
import com.concertfinder.concertfinder.chat_room_and_user.domain.ChatRoomAndUser;
import com.concertfinder.concertfinder.chat_room_and_user.repository.ChatRoomAndUserRepository;
import com.concertfinder.concertfinder.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomAndUserService {

    private final ChatRoomAndUserRepository chatRoomAndUserRepository;

    private final UserService userService;

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

    // 1 : 1 채팅방 유저 저장 메서드
    public void insertPrivateChatRoomAndUser(long userId, long roomId) {

        ChatRoomAndUser chatRoomAndUser = ChatRoomAndUser.builder()
                .userId(userId)
                .roomId(roomId)
                .isHost(false)
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


    public boolean isHost(long userId, long roomId) {

        Optional<ChatRoomAndUser> optionalChatRoomAndUser = chatRoomAndUserRepository.findByUserIdAndRoomId(userId, roomId);

        if(!optionalChatRoomAndUser.isPresent()) {

            throw new NoSuchElementException("유저 정보를 찾을 수 없습니다!");
        }

        return optionalChatRoomAndUser.get().isHost();
    }

    public int getCurrentCount(long roomId) {

        return chatRoomAndUserRepository.countByRoomId(roomId);
    }

    public List<ChatUserInfoDTO> getUserInfoList(Long roomId) {

        if(roomId == null) {
            throw new IllegalStateException("채팅방 번호가 비어있습니다!");
        }

        List<ChatRoomAndUser> chatRoomAndUsers = chatRoomAndUserRepository.findAllByRoomId(roomId);

        List<ChatUserInfoDTO> userInfoList = new ArrayList<>();

        for(ChatRoomAndUser c : chatRoomAndUsers) {

            ChatUserInfoDTO chatUserInfoDTO = ChatUserInfoDTO.builder()
                    .id(c.getUserId())
                    .roomId(roomId)
                    .nickname(userService.getNickname(c.getUserId()))
                    .isHost(isHost(c.getUserId(), roomId))
                    .build();

            userInfoList.add(chatUserInfoDTO);
        }

        return userInfoList;
    }

    public List<Long> getTop3ChatRoomIdList(long userId) {

        List<ChatRoomAndUser> chatRoomAndUsers = chatRoomAndUserRepository.findTop3ByUserIdOrderByCreatedAtDesc(userId);

        List<Long> chatRoomIdList = new ArrayList<>();

        for(ChatRoomAndUser c : chatRoomAndUsers) {

            chatRoomIdList.add(c.getRoomId());
        }

        return chatRoomIdList;
    }

    public List<Long> getChatRoomIdList(long userId) {

        List<ChatRoomAndUser> chatRoomAndUsers = chatRoomAndUserRepository.findAllByUserIdOrderByCreatedAtDesc(userId);

        List<Long> chatRoomIdList = new ArrayList<>();

        for(ChatRoomAndUser c : chatRoomAndUsers) {

            chatRoomIdList.add(c.getRoomId());
        }

        return chatRoomIdList;
    }

    // 1:1 채팅 상대방 닉네임 반환
    public String getPrivateRoomNickname(long roomId, long userId) {

        ChatRoomAndUser chatRoomAndUser = chatRoomAndUserRepository.findByRoomIdAndUserIdNot(roomId, userId).orElse(null);

        if(chatRoomAndUser == null) {

            throw new NoSuchElementException("채팅방 리스트를 불러오는 중 에러가 발생 했습니다!");
        }

        return userService.getNickname(chatRoomAndUser.getUserId());
    }
}
