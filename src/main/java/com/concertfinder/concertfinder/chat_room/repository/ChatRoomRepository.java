package com.concertfinder.concertfinder.chat_room.repository;

import com.concertfinder.concertfinder.chat_room.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByAccompanyId(long accompanyId);

    @Query(value = "SELECT u.room_id " +
            "FROM chat_room_and_user AS u " +
            "JOIN chat_room_and_user AS ou ON u.room_id = ou.room_id " +
            "JOIN chat_room AS c ON u.room_id = c.id " +
            "WHERE u.user_id = :userId " +
            "AND ou.user_id = :otherUserId " +
            "AND c.accompany_id IS NULL " +
            "AND c.room_name IS NULL " +
            "LIMIT 1", nativeQuery = true)
    Optional<Long> findRoomIdByNativeQuery(long userId, long otherUserId);


}
