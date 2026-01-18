package com.concertfinder.concertfinder.chatMessage.repository;

import com.concertfinder.concertfinder.chatMessage.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {


    public List<ChatMessage> findAllByRoomId(long roomId);
}
