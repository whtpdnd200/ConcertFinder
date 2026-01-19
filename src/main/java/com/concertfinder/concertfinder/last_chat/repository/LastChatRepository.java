package com.concertfinder.concertfinder.last_chat.repository;

import com.concertfinder.concertfinder.last_chat.domain.LastChat;
import com.concertfinder.concertfinder.last_chat.domain.LastChatId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LastChatRepository extends JpaRepository<LastChat, LastChatId> {


    public Optional<LastChat> findByRoomIdAndUserId(long roomId, long userId);
}
