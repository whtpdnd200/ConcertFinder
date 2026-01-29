package com.concertfinder.concertfinder.accompany_count.repository;

import com.concertfinder.concertfinder.accompany_count.domain.AccompanyCount;
import com.concertfinder.concertfinder.accompany_count.domain.AccompanyCountId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccompanyCountRepository extends JpaRepository<AccompanyCount, AccompanyCountId> {


    int countByAccompanyId(long accompanyId);

    Optional<AccompanyCount> findByAccompanyIdAndUserId(long accompanyId, long userId);

    List<AccompanyCount> findAllByAccompanyId(long accompanyId);

    boolean existsByAccompanyIdAndUserId(long accompanyId, long userId);

    List<AccompanyCount> findAllTop3ByUserId(long userId);

    List<AccompanyCount> findAllByUserId(long userId);

    List<AccompanyCount> findAllByAccompanyIdIn(List<Long> accompanyIdList);
}
