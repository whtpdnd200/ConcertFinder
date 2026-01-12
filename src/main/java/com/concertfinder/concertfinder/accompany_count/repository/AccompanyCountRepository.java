package com.concertfinder.concertfinder.accompany_count.repository;

import com.concertfinder.concertfinder.accompany_count.domain.AccompanyCount;
import com.concertfinder.concertfinder.accompany_count.domain.AccompanyCountId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccompanyCountRepository extends JpaRepository<AccompanyCount, AccompanyCountId> {


    public int countByAccompanyId(long accompanyId);

    public Optional<AccompanyCount> findByAccompanyIdAndUserId(long accompanyId, long userId);

    public List<AccompanyCount> findAllByAccompanyId(long accompanyId);
}
