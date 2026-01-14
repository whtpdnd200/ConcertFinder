package com.concertfinder.concertfinder.chat_room.repository;

import com.concertfinder.concertfinder.chat_room.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    public Optional<ChatRoom> findByAccompanyId(long accompanyId);
}
