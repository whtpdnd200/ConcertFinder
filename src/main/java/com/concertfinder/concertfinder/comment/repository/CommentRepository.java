package com.concertfinder.concertfinder.comment.repository;

import com.concertfinder.concertfinder.comment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {


    public Page<Comment> findAllByPostId(long postId, Pageable pageable);
    
    public int countByPostId(long postId);
}
