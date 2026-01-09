package com.concertfinder.concertfinder.review.repository;

import com.concertfinder.concertfinder.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {


    public int countByAreaCode(String areaCode);

    @Query("""
            SELECT AVG(r.point) FROM Review r WHERE r.areaCode = :areaCode
            """)
    public Double getAveragePointByAreaCode(String areaCode);

    public Page<Review> findAllByAreaCode(String areaCode, Pageable pageable);

}
