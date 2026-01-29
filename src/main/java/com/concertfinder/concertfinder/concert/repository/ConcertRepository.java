package com.concertfinder.concertfinder.concert.repository;

import com.concertfinder.concertfinder.concert.domain.Concert;
import com.concertfinder.concertfinder.favorites.DTO.FavoritesConcertIdDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConcertRepository extends JpaRepository<Concert, String> {

    boolean existsByConcertId(String concertId);

    @Query("""
            SELECT c FROM Concert c
            WHERE c.concertId IN(:concertIdList)
            ORDER BY c.createdAt DESC
            """)
    List<Concert> findAllByConcertId(List<String> concertIdList);

    @Query("""
            SELECT c FROM Concert c
            WHERE c.concertId IN(:concertIdList)
            ORDER BY c.createdAt DESC
            """)
    List<Concert> findAllTop3ByConcertId(List<String> concertIdList, Pageable pageable);

    List<Concert> findAllByConcertId(String concertId);


}
