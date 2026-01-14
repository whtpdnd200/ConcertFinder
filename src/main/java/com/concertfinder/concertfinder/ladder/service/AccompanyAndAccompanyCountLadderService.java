package com.concertfinder.concertfinder.ladder.service;

import com.concertfinder.concertfinder.accompany.DTO.AccompanyAddDTO;
import com.concertfinder.concertfinder.accompany.DTO.AccompanyInfoDTO;
import com.concertfinder.concertfinder.accompany.domain.Accompany;
import com.concertfinder.concertfinder.accompany.service.AccompanyService;
import com.concertfinder.concertfinder.accompany_count.service.AccompanyCountService;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@lombok.extern.slf4j.Slf4j
@Service
@RequiredArgsConstructor
@Slf4j
public class AccompanyAndAccompanyCountLadderService {

    private final AccompanyService accompanyService;

    private final AccompanyCountService accompanyCountService;

    private final ChatRoomAndChatRoomAndUserLadderService chatRoomAndChatRoomAndUserLadderService;

    // 동행 모집 정보 생성 및 동행 모집 인원 추가
    @Transactional
    public void insertAccompanyAndAccompanyCount(AccompanyAddDTO accompanyAddDTO, long userId, String roomName) {

        long accompanyId = accompanyService.insertAccompany(accompanyAddDTO);

        accompanyCountService.insertAccompanyCount(accompanyId, accompanyAddDTO.getUserId());

        chatRoomAndChatRoomAndUserLadderService.insertChatRoomAndChatAndUser(accompanyId, userId, roomName);
    }

    // 동행 모집 정보 삭제 및 동행 모집 인원 전체 삭제
    @Transactional
    public void deleteAccompanyAndAccompanyCount(long postId) {

        long accompanyId = accompanyService.getAccompanyId(postId);

        accompanyService.deleteAccompany(accompanyId);
        accompanyCountService.deleteAllAccompanyCount(accompanyId);

        long roomId = chatRoomAndChatRoomAndUserLadderService.getRoomId(accompanyId);

        chatRoomAndChatRoomAndUserLadderService.deleteChatRoom(roomId);
        chatRoomAndChatRoomAndUserLadderService.deleteAllChatRoomAndUser(roomId);
    }

    // 동행 인원 신청 및 동행 모집 인원 체크
    @Transactional
    public void insertAccompanyCountAndIsFullCheck(long accompanyId, long userId) {
        Accompany accompany = accompanyService.getAccompany(accompanyId);

        accompanyCountService.insertAccompanyCount(accompanyId, userId);

        if(accompanyService.isFull( accompany.getHeadCount(), accompany.getId())) {

            accompanyService.isFullChange(accompany, true);
        }

        long roomId = chatRoomAndChatRoomAndUserLadderService.getRoomId(accompanyId);

        chatRoomAndChatRoomAndUserLadderService.insertChatRoomAndUser(userId, roomId);
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
}
