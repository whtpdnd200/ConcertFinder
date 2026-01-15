package com.concertfinder.concertfinder.chatMessage.repository;

import com.concertfinder.concertfinder.chatMessage.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {


}
