package com.concertfinder.concertfinder.common;

import com.concertfinder.concertfinder.chat_message.DTO.SendMessageDTO;
import com.concertfinder.concertfinder.chat_message.service.ChatMessageService;
import com.concertfinder.concertfinder.ladder.service.ChatRoomAndChatRoomAndUserLadderService;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@lombok.extern.slf4j.Slf4j
@Component
@RequiredArgsConstructor
@Slf4j
public class SimpleWebSocketHandler extends TextWebSocketHandler {

    private final Map<Long, Set<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();

    private final ChatMessageService chatMessageService;


    public void sendMessageInsert(String[] parts, long userId) {

        SendMessageDTO sendMessageDTO = SendMessageDTO.builder()
                .userId(userId)
                .roomId(Long.parseLong(parts[0]))
                .messageType(parts[1])
                .content(parts[3])
                .build();

        chatMessageService.insertMessage(sendMessageDTO);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        Long roomId = (Long)session.getAttributes().get("roomId");
        if(roomId != null) {
            roomSessions.computeIfAbsent(roomId, k ->
                    ConcurrentHashMap.newKeySet()).add(session);
        }

        log.info("connected : {} ", session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        Long userId = (Long)session.getAttributes().get("userId");

        // 규격: [0]방번호 | [1]타입 | [2]닉네임 | [3]메시지

        String payload = message.getPayload();

        // |의 뒤가 공백이어도 전부 스플릿
        String[] parts = payload.split("\\|", -1);

        StringBuilder sb = new StringBuilder(payload);

        if(parts[1].equals("DISCONNECT")) {

            sb.append("님이 퇴장 하셧습니다.");
            parts[3] = "님이 퇴장 하셧습니다.";
        }

        if(parts[1].equals("KICK")) {
            sb.append("님이 관리자에 의해 강퇴 당하셧습니다.");

            parts[3] = "님이 관리자에 의해 강퇴 당하셧습니다.";
        }

        Long roomId = Long.parseLong(parts[0]);

        Set<WebSocketSession> sessions = roomSessions.get(roomId);

        log.info("sb : {} ", sb.toString());

        if(sessions != null) {
            for(WebSocketSession s : sessions) {

                if(s.isOpen()) {
                    s.sendMessage(new TextMessage(sb.toString()));
                }
            }
        }

        sendMessageInsert(parts, userId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        Long roomId = (Long)session.getAttributes().get("roomId");
        long userId = (Long)session.getAttributes().get("userId");


        if(roomId != null) {
            Set<WebSocketSession> sessions = roomSessions.get(roomId);

            sessions.remove(session);

            chatMessageService.updateLastChat(roomId, userId);
        }
    }

    public void sendEnterMessage(long roomId, String userNickname, long userId) throws Exception {

        Set<WebSocketSession> sessions = roomSessions.get(roomId);

        String sendOpenMessage = roomId + "|" + "CONNECT" + "|" + userNickname + "|님이 입장 했습니다.";

        String[] parts = sendOpenMessage.split("\\|", -1);

        if(sessions != null) {

            for(WebSocketSession s : sessions) {

                if(s.isOpen()) {
                    s.sendMessage(new TextMessage(sendOpenMessage));
                }
            }
        }

        sendMessageInsert(parts, userId);
    }

    public void deleteRoom(long roomId) {
        Set<WebSocketSession> sessions = roomSessions.get(roomId);

        if(sessions != null) {
            for(WebSocketSession s : sessions) {

                try {

                    s.sendMessage(new TextMessage(roomId + "|DELETE|SYSTEM|방이 삭제 되었습니다."));
                    s.close();

                } catch(IOException e) {

                    throw new RuntimeException("에러발생!");
                }
            }
        }
    }

    public void kickUser(long roomId,long userId) {

        Set<WebSocketSession> sessions = roomSessions.get(roomId);

        String message = roomId + "|KICK_USER|SYSTEM|관리자에 의해 강퇴되었습니다.";

        String[] parts = message.split("\\|", -1);

        if(sessions != null) {
            for(WebSocketSession s : sessions) {

                Long kickUserId = (Long)s.getAttributes().get("userId");

                if(kickUserId.equals(userId)) {

                    try {
                        s.sendMessage(new TextMessage(roomId + "|KICK_USER|SYSTEM|관리자에 의해 강퇴되었습니다."));
                        s.close();

                    } catch(IOException e) {

                        throw new RuntimeException("유저 강퇴중 에러가 발생 했습니다!");
                    }
                    break;
                }
            }
        }

    }
}
