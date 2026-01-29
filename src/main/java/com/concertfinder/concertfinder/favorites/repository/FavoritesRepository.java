package com.concertfinder.concertfinder.favorites.repository;

import com.concertfinder.concertfinder.favorites.domain.Favorites;
import com.concertfinder.concertfinder.favorites.domain.FavoritesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FavoritesRepository extends JpaRepository<Favorites, FavoritesId> {

    boolean existsByUserIdAndConcertId(long userId, String concertId);

    boolean existsByConcertId(String concertId);

    Optional<Favorites> findByUserIdAndConcertId(long userId, String concertId);

    List<Favorites> findAllByUserId(long userId);

    void deleteAllByUserIdIn(List<Long> userIdList);


    @Query(value = "SELECT f.* FROM `favorites` AS f" +
            " JOIN `concert` AS c" +
            " ON f.concert_id = c.concert_id" +
            " WHERE c.concert_date = :tomorrow", nativeQuery = true)
    List<Favorites> selectByConcertJoin(@Param("tomorrow")LocalDate tomorrow);
}
