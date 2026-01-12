package com.concertfinder.concertfinder.accompany.repository;

import com.concertfinder.concertfinder.accompany.domain.Accompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccompanyRepository extends JpaRepository<Accompany, Long> {


}
