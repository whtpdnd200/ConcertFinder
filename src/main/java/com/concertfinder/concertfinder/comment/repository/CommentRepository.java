package com.concertfinder.concertfinder.comment.repository;

import com.concertfinder.concertfinder.comment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {


    Page<Comment> findAllByPostId(long postId, Pageable pageable);
    
    int countByPostId(long postId);

    List<Comment> findAllByPostId(long postId);

    void deleteAllByUserIdIn(List<Long> userIds);
}
