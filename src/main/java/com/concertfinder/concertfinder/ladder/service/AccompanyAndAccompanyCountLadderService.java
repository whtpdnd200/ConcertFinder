package com.concertfinder.concertfinder.ladder.service;

import com.concertfinder.concertfinder.accompany.DTO.AccompanyAddDTO;
import com.concertfinder.concertfinder.accompany.DTO.AccompanyInfoDTO;
import com.concertfinder.concertfinder.accompany.domain.Accompany;
import com.concertfinder.concertfinder.accompany.service.AccompanyService;
import com.concertfinder.concertfinder.accompany_count.service.AccompanyCountService;
import com.concertfinder.concertfinder.chat_room.DTO.ChatRoomListDTO;
import com.concertfinder.concertfinder.common.SimpleWebSocketHandler;
import com.concertfinder.concertfinder.exception.custom_exception.UnAuthorizedException;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@lombok.extern.slf4j.Slf4j
@Service
@RequiredArgsConstructor
@Slf4j
public class AccompanyAndAccompanyCountLadderService {

    private final AccompanyService accompanyService;

    private final AccompanyCountService accompanyCountService;

    private final ChatRoomAndChatRoomAndUserLadderService chatRoomAndChatRoomAndUserLadderService;

    private final SimpleWebSocketHandler simpleWebSocketHandler;

    // 동행 모집 정보 생성 및 동행 모집 인원 추가
    @Transactional
    public void insertAccompanyAndAccompanyCount(AccompanyAddDTO accompanyAddDTO, long userId, String roomName) {

        long accompanyId = accompanyService.insertAccompany(accompanyAddDTO);

        accompanyCountService.insertAccompanyCount(accompanyId, accompanyAddDTO.getUserId());

        chatRoomAndChatRoomAndUserLadderService.insertChatRoomAndChatAndUser(accompanyId, userId, roomName);
    }

    // 게시글 PK로 동행 모집 정보 삭제 및 동행 모집 인원 전체 삭제
    @Transactional
    public void deleteAccompanyAndAccompanyCount(long postId) {

        long accompanyId = accompanyService.getAccompanyId(postId);

        accompanyService.deleteAccompany(accompanyId);
        accompanyCountService.deleteAllAccompanyCount(accompanyId);

        long roomId = chatRoomAndChatRoomAndUserLadderService.getRoomId(accompanyId);

        chatRoomAndChatRoomAndUserLadderService.deleteChatRoom(roomId);
        chatRoomAndChatRoomAndUserLadderService.deleteAllChatRoomAndUser(roomId);
    }

    // 동행 모집 PK로 동행 모집 정보 삭제 및 동행 모집 인원 전체 삭제
    @Transactional
    public void deleteAccompanyAndAccompanyCountByAccompanyId(long accompanyId, long roomId, long userId) {

        if(!chatRoomAndChatRoomAndUserLadderService.isHost(userId, roomId)) {

            throw new UnAuthorizedException("관리자만 채팅방을 삭제 할 수 있습니다!");
        }
        accompanyService.deleteAccompany(accompanyId);
        accompanyCountService.deleteAllAccompanyCount(accompanyId);

        chatRoomAndChatRoomAndUserLadderService.deleteChatRoom(roomId);
        chatRoomAndChatRoomAndUserLadderService.deleteAllChatRoomAndUser(roomId);
    }

    // 동행 인원 신청 및 동행 모집 인원 체크
    @Transactional
    public void insertAccompanyCountAndIsFullCheck(long accompanyId, long userId, String nickName) {
        Accompany accompany = accompanyService.getAccompany(accompanyId);

        accompanyCountService.insertAccompanyCount(accompanyId, userId);

        if(accompanyService.isFull( accompany.getHeadCount(), accompany.getId())) {

            accompanyService.isFullChange(accompany, true);
        }

        long roomId = chatRoomAndChatRoomAndUserLadderService.getRoomId(accompanyId);

        chatRoomAndChatRoomAndUserLadderService.insertChatRoomAndUser(userId, roomId);

        try {
            simpleWebSocketHandler.sendEnterMessage(roomId, nickName);
        } catch(Exception e) {

            throw new RuntimeException("서버에러로 인해 채팅방에 입장하지 못했습니다!");
        }
    }
    

    // 동행 모집 정보 반환
    public AccompanyInfoDTO getAccompanyInfo(long postId, long userId) {

        return accompanyService.getAccompanyInfo(postId, userId);
    }

    // 동행 신청 취소 및 동행 인원 상태 변경
    @Transactional
    public void deleteAccompanyCountAndIsFullCheck(long accompanyId, long userId) {

        Accompany accompany = accompanyService.getAccompany(accompanyId);

        accompanyCountService.deleteAccompanyCount(accompanyId, userId);

        if(!accompanyService.isFull(accompany.getHeadCount(), accompany.getId())) {
            accompanyService.isFullChange(accompany, false);
        }

        long roomId = chatRoomAndChatRoomAndUserLadderService.getRoomId(accompanyId);

        chatRoomAndChatRoomAndUserLadderService.deleteChatRoomAndUser(userId, roomId);
    }

    // 참여중인 채팅방 목록 3개 리스트 반환 메서드
    public List<ChatRoomListDTO> getTop3ChatRoomList(long userId) {

        List<Long> accompanyIdList = accompanyCountService.getAccompanyIdTop3List(userId);

        List<ChatRoomListDTO> chatRoomList = new ArrayList<>();

        for(long accompanyId : accompanyIdList) {

            Accompany accompany = accompanyService.getAccompany(accompanyId);
            long roomId = chatRoomAndChatRoomAndUserLadderService.getRoomId(accompanyId);
            String roomName = chatRoomAndChatRoomAndUserLadderService.getChatName(roomId);

            ChatRoomListDTO chatRoomListDTO = ChatRoomListDTO.builder()
                    .roomId(roomId)
                    .roomName(roomName)
                    .headCount(accompany.getHeadCount())
                    .currentCount(accompanyCountService.getAccompanyCount(accompanyId))
                    .isFull(accompany.isFull())
                    .isDateAfter(accompanyService.compareDate(accompany.getSDateTime()))
                    .build();

            chatRoomList.add(chatRoomListDTO);
        }
        return chatRoomList;
    }

    // 참여중인 전체 채팅 목록 반환
    public List<ChatRoomListDTO> getChatRoomList(long userId) {

        List<Long> accompanyIdList = accompanyCountService.getAccompanyAllIdList(userId);

        List<ChatRoomListDTO> allChatRoomList = new ArrayList<>();

        for(long accompanyId : accompanyIdList) {

            Accompany accompany = accompanyService.getAccompany(accompanyId);
            long roomId = chatRoomAndChatRoomAndUserLadderService.getRoomId(accompanyId);
            String roomName = chatRoomAndChatRoomAndUserLadderService.getChatName(roomId);

            ChatRoomListDTO chatRoomListDTO = ChatRoomListDTO.builder()
                    .roomId(roomId)
                    .roomName(roomName)
                    .headCount(accompany.getHeadCount())
                    .currentCount(accompanyCountService.getAccompanyCount(accompanyId))
                    .isFull(accompany.isFull())
                    .isDateAfter(accompanyService.compareDate(accompany.getSDateTime()))
                    .build();

            allChatRoomList.add(chatRoomListDTO);
        }
        return allChatRoomList;
    }

    public long getPostId(long accompanyId) {

        return accompanyService.getPostId(accompanyId);
    }
}
