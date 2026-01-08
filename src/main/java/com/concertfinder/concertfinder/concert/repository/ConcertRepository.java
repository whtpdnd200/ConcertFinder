package com.concertfinder.concertfinder.concert.repository;

import com.concertfinder.concertfinder.concert.domain.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcertRepository extends JpaRepository<Concert, String> {


}
