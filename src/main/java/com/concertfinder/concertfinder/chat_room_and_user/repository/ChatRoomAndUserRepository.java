package com.concertfinder.concertfinder.chat_room_and_user.repository;

import com.concertfinder.concertfinder.chat_room_and_user.domain.ChatRoomAndUser;
import com.concertfinder.concertfinder.chat_room_and_user.domain.ChatRoomAndUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomAndUserRepository extends JpaRepository<ChatRoomAndUser, ChatRoomAndUserId> {

    boolean existsByRoomId(long roomId);

    List<ChatRoomAndUser> findAllByRoomId(long roomId);

    Optional<ChatRoomAndUser> findByUserIdAndRoomId(long userId, long roomId);

    int countByRoomId(long roomId);

    List<ChatRoomAndUser> findTop3ByUserIdOrderByCreatedAtDesc(long userId);

    List<ChatRoomAndUser> findAllByUserIdOrderByCreatedAtDesc(long userId);

    Optional<ChatRoomAndUser> findByRoomIdAndUserIdNot(long roomId, long userId);

    List<ChatRoomAndUser> findAllByUserIdIn(List<Long> userIdList);
}
