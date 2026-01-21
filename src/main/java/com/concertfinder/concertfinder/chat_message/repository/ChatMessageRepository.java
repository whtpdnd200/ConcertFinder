package com.concertfinder.concertfinder.chat_message.repository;

import com.concertfinder.concertfinder.chat_message.domain.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {


    public List<ChatMessage> findAllByRoomId(long roomId);

    Slice<ChatMessage> findAllByRoomIdOrderByIdDesc(long roomId, Pageable pageable);

    Slice<ChatMessage> findByRoomIdAndIdLessThanOrderByIdDesc(long roomId, long id, Pageable pageable);

    public Optional<ChatMessage> findFirstByRoomIdOrderByIdDesc(long roomId);

    // 과거 메시지 조회 (DESC)
    Slice<ChatMessage> findByRoomIdAndIdLessThanEqualOrderByIdDesc(long roomId, Long lastId, Pageable pageable);

    // 이후 메시지 조회 (ASC)
    Slice<ChatMessage> findByRoomIdAndIdGreaterThanOrderByIdAsc(Long roomId, Long lastId, Pageable pageable);

}
