package com.concertfinder.concertfinder.review.repository;

import com.concertfinder.concertfinder.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {


    public int countByAreaCode(String areaCode);

    @Query("""
            SELECT AVG(r.point) FROM Review r WHERE r.areaCode = :areaCode
            """)
    public double getAveragePointByAreaCode(String areaCode);
}
