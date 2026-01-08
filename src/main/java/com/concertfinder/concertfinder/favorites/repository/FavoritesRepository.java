package com.concertfinder.concertfinder.favorites.repository;

import com.concertfinder.concertfinder.favorites.domain.Favorites;
import com.concertfinder.concertfinder.favorites.domain.FavoritesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoritesRepository extends JpaRepository<Favorites, FavoritesId> {

    public boolean existsByUserIdAndConcertId(long userId, String concertId);

    public Optional<Favorites> findByUserIdAndConcertId(long userId, String concertId);

    public List<Favorites> findAllByUserId(long userId);
}
