package com.concertfinder.concertfinder.favorites.repository;

import com.concertfinder.concertfinder.favorites.domain.Favorites;
import com.concertfinder.concertfinder.favorites.domain.FavoritesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoritesRepository extends JpaRepository<Favorites, FavoritesId> {

    public boolean existsByUserIdAndConcertId(long userId, String concertId);
}
