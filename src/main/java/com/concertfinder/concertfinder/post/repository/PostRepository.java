package com.concertfinder.concertfinder.post.repository;

import com.concertfinder.concertfinder.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {


    public Page<Post> findAllByConcertId(String concertId, Pageable pageable);
}
