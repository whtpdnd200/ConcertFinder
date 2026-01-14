package com.concertfinder.concertfinder.chat_room_and_user.repository;

import com.concertfinder.concertfinder.chat_room_and_user.domain.ChatRoomAndUser;
import com.concertfinder.concertfinder.chat_room_and_user.domain.ChatRoomAndUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomAndUserRepository extends JpaRepository<ChatRoomAndUser, ChatRoomAndUserId> {

    public boolean existsByRoomId(long roomId);

    public List<ChatRoomAndUser> findAllByRoomId(long roomId);

    public Optional<ChatRoomAndUser> findByUserIdAndRoomId(long userId, long roomId);
}
