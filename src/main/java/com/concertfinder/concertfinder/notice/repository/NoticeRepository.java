package com.concertfinder.concertfinder.notice.repository;

import com.concertfinder.concertfinder.notice.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    List<Notice> findAllByReceiverIdAndIsReadFalseOrderByIdDesc(long receiverId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE `notice` SET `is_read` = true WHERE `receiver_id` = :userId", nativeQuery = true)
    void updatedAllByUserId(long userId);
}
