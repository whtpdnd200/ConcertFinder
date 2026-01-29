package com.concertfinder.concertfinder.accompany.repository;

import com.concertfinder.concertfinder.accompany.domain.Accompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccompanyRepository extends JpaRepository<Accompany, Long> {


    Optional<Accompany> findByPostId(long postId);

    @Query(value = "SELECT * FROM `accompany`" +
            "WHERE DATE(`s_date_time`) = :tomorrow", nativeQuery = true)
    List<Accompany> selectAllByStartDate(@Param("tomorrow")LocalDate tomorrow);
}
