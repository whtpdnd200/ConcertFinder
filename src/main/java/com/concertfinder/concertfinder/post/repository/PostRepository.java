package com.concertfinder.concertfinder.post.repository;

import com.concertfinder.concertfinder.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {


    Page<Post> findAllByConcertId(String concertId, Pageable pageable);

    long countByConcertId(String concertId);

    Page<Post> findAllByConcertIdAndCategory(String concertId, char category, Pageable pageable);

    long countByConcertIdAndCategory(String concertId, char category);

    List<Post> findAllByUserIdIn(List<Long> userIdList);
}
